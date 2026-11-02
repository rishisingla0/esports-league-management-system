# Esports Tournament Management System

## Backend Progress & Developer Handover

**Checkpoint:** 2 November 2026\
**Backend module:** `Backend/esports-api`\
**Database:** MySQL 8 --- `esports_db`\
**Stack:** Java 21, Spring Boot 4.1.2, Spring Data JPA, Hibernate, Maven

## 1. Project overview

The Esports Tournament Management System is intended to manage esports
games, organizations, teams, players, tournaments, matches, performance
data and analytics.

The long-term direction is a multi-game platform. Shared platform
functionality should be reusable across games, while game-specific
rules---such as roster size, match format, scoring and performance
metrics---should be represented explicitly.

**Database scope note:** The existing schema contains some
battle-royale-oriented assumptions. For example, match squad validation
currently enforces exactly four players. Review these rules before
treating the match workflow as fully compatible with every esports
title.

## 2. Technology and local setup

  Component        Current setup
  ---------------- -----------------------------
  Language         Java 21
  Framework        Spring Boot 4.1.2
  Persistence      Spring Data JPA / Hibernate
  Build            Maven Wrapper
  Database         MySQL 8.0.46
  Schema           `esports_db`
  Local API port   `8080`

### Prerequisites

-   JDK 21
-   MySQL Server 8
-   VS Code terminal or another PowerShell terminal

### Database configuration

Configuration file:

`Backend/esports-api/src/main/resources/application.properties`

Relevant settings:

``` properties
spring.application.name=esports-api

spring.datasource.url=jdbc:mysql://localhost:3306/esports_db
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.open-in-view=false

spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```

The database password is read from the `DB_PASSWORD` environment
variable. Never commit passwords or put them in shared documentation.

Set it for the current PowerShell session without displaying it:

``` powershell
$secure = Read-Host "Enter MySQL root password" -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new("", $secure).Password
```

This variable is session-specific. Set it again in a new terminal unless
a secure persistent configuration has been established.

### Compile and run

From the backend module directory:

``` powershell
cd Backend\esports-api
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run
```

A successful compile ends with `BUILD SUCCESS`. The API runs at
`http://localhost:8080`. Keep the server terminal running while testing
from a second terminal.

## 3. Database setup and current sample data

The SQL scripts are in the repository's `database/` directory. Scripts
numbered `01` through `35` were executed in order against `esports_db`.

The schema contains 17 core tables:

1.  `games`
2.  `organizations`
3.  `coaches`
4.  `teams`
5.  `players`
6.  `player_game_profiles`
7.  `team_rosters`
8.  `player_transfers`
9.  `tournaments`
10. `tournament_registrations`
11. `matches`
12. `match_teams`
13. `match_squad_players`
14. `player_performances`
15. `sponsors`
16. `team_sponsorships`
17. `prize_distributions`

The database includes primary keys, foreign keys, unique constraints,
check constraints, indexes and triggers for validation and historical
protection.

### Sample data currently present

The initial sample dataset includes:

-   Games: BGMI and Free Fire
-   Organizations: Phoenix Esports and Tiger Gaming
-   Teams: Phoenix BGMI, Tigers BGMI and Phoenix Free Fire
-   Players and game profiles
-   BGMI Championship 2026 and Free Fire Cup 2026
-   Tournament registrations
-   One scheduled BGMI match

During API testing, two additional teams were created:

-   `Tiger Free Fire` --- team ID `4`
-   `Tiger Free Fire Academy` --- team ID `5`

These are records in the development database. Another developer's local
database will not automatically contain them.

The sample match is scheduled and has no completed match results, squads
or player performance records. Analytics queries that depend on
completed matches may therefore return zero totals. That is expected for
the current sample state.

## 4. Backend project structure

The Spring Boot application is under:

`Backend/esports-api/src/main/java/com/esports/api/`

  Package        Responsibility
  -------------- -------------------------------------------
  `controller`   HTTP routes and request/response handling
  `service`      Application logic and validation
  `repository`   Database access through Spring Data JPA
  `entity`       Java mappings to database tables
  `dto`          API request and response structures

The application entry point is `EsportsApiApplication.java`.

## 5. Existing Game API

The Game module was implemented and tested before the Team module.

  Method   Endpoint            Purpose
  -------- ------------------- --------------------
  GET      `/api/games`        Retrieve all games
  GET      `/api/games/{id}`   Retrieve one game
  POST     `/api/games`        Create a game

`Game` maps to the `games` table. `GameRepository` extends
`JpaRepository<Game, Long>`. `GameService` handles retrieval and
creation, and `GameController` exposes the routes.

`CreateGameRequest` is a Java record used for request validation. It
uses `@NotBlank` and `@Size` constraints for required text fields.

The health endpoint is `GET /api/health`. It was tested and returned a
healthy status with the database connected.

## 6. Team database model

The `teams` table has these columns:

  Column              Meaning
  ------------------- -----------------------------------------
  `team_id`           Primary key; auto-increment
  `organization_id`   Required foreign key to `organizations`
  `game_id`           Required foreign key to `games`
  `coach_id`          Optional foreign key to `coaches`
  `team_name`         Required team name
  `created_at`        Database-generated timestamp

There is a composite unique constraint on
`(organization_id, game_id, team_name)`. A team name must be unique
within the same organization and game.

### Relationships

-   A Team belongs to one Organization.
-   A Team belongs to one Game.
-   A Team may have one Coach; the coach relationship is optional.

The Team entity uses `@ManyToOne(fetch = FetchType.LAZY)` for these
relationships. This avoids automatically loading related objects
whenever a Team is fetched.

## 7. Team module implementation

### Entities

-   `Team.java` --- maps to `teams`.
-   `Organization.java` --- maps to `organizations`.
-   `Coach.java` --- maps to `coaches`.
-   `Game.java` --- existing mapping to `games`.

The Team timestamp field is database-managed:

``` java
@Column(name = "created_at", insertable = false, updatable = false)
private LocalDateTime createdAt;
```

The database supplies this timestamp; the application does not insert or
update it directly.

### Repositories

-   `TeamRepository`
-   `OrganizationRepository`
-   `CoachRepository`
-   Existing `GameRepository`

These extend `JpaRepository`, which supplies operations such as
`findAll`, `findById`, `save` and `saveAndFlush`.

`TeamRepository` also defines a derived existence query to check a team
name within an organization/game combination:

``` java
boolean existsByOrganizationOrganizationIdAndGameGameIdAndTeamNameIgnoreCase(
        Long organizationId,
        Long gameId,
        String teamName
);
```

### Request DTO

`CreateTeamRequest` is a Java record used for POST requests:

``` java
public record CreateTeamRequest(
        @NotNull Long organizationId,
        @NotNull Long gameId,
        Long coachId,
        @NotBlank @Size(max = 100) String teamName
) {}
```

`organizationId` and `gameId` are required. `coachId` is optional.
`teamName` is required and limited to 100 characters.

### Response DTO

`TeamResponse` is used instead of returning nested JPA entities
directly. It exposes:

-   `teamId`
-   `teamName`
-   `organizationId`
-   `organizationName`
-   `gameId`
-   `gameName`
-   `coachId`
-   `coachName`
-   `createdAt`

This provides a controlled response shape and avoids serializing
lazy-loaded entity relationships directly.

### Service layer

`TeamService` currently handles:

-   Retrieving all teams
-   Retrieving a team by ID
-   Creating a team
-   Checking that the referenced organization and game exist
-   Checking for duplicate team names within the organization/game
    combination
-   Looking up an optional coach when a coach ID is supplied
-   Converting a Team entity to `TeamResponse`

Read methods use `@Transactional(readOnly = true)`. Creation uses
`@Transactional`.

### Controller layer

`TeamController` exposes routes under `/api/teams` and delegates
application logic to `TeamService`.

## 8. Team API routes implemented and tested

  Method   Endpoint            Status                Description
  -------- ------------------- --------------------- -------------------
  GET      `/api/teams`        Tested successfully   Returns all teams
  GET      `/api/teams/{id}`   Tested successfully   Returns one team
  POST     `/api/teams`        Tested successfully   Creates a team

### GET all teams

PowerShell:

``` powershell
Invoke-RestMethod http://localhost:8080/api/teams | ConvertTo-Json -Depth 5
```

This returned the three initial sample teams, with team, organization
and game identifiers/names, nullable coach fields and timestamps.

### GET one team

``` powershell
Invoke-RestMethod http://localhost:8080/api/teams/1 | ConvertTo-Json -Depth 5
```

This returned `Phoenix BGMI` with its organization and game details.

### POST create a team

Example:

``` powershell
$body = @{
    organizationId = 2
    gameId = 2
    teamName = "Example Free Fire Team"
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri http://localhost:8080/api/teams `
    -Method Post `
    -ContentType "application/json" `
    -Body $body |
    ConvertTo-Json -Depth 5
```

The API returns the created `TeamResponse`, including its generated ID
and timestamp.

### Why EntityManager.refresh() is used

MySQL generates `created_at` automatically. After insertion, the
in-memory entity may not yet contain that database-generated timestamp.

The current creation flow is:

``` java
Team savedTeam = teamRepository.saveAndFlush(team);
entityManager.refresh(savedTeam);

return convertToResponse(savedTeam);
```

-   `saveAndFlush()` saves the entity and flushes the INSERT to the
    database.
-   `entityManager.refresh(savedTeam)` reloads the entity's database
    state, including the generated timestamp.
-   `convertToResponse(savedTeam)` maps the refreshed entity to the
    response DTO.

`EntityManager` is injected into `TeamService` through its constructor.

This was tested successfully: the POST response for
`Tiger Free Fire Academy` returned a non-null `createdAt` value.

## 9. Important implementation notes

### Why use DTOs?

Returning JPA entities directly can expose persistence details and cause
serialization problems when relationships are lazy-loaded.
`TeamResponse` provides a controlled API shape and includes only the
fields the client needs.

### Why use transactions for reads?

The Team-to-Organization, Team-to-Game and Team-to-Coach associations
are lazy. Mapping those relationships to response fields should happen
while the persistence context is active. The read service methods are
therefore transactional.

### Duplicate handling

Before creating a Team, the service checks whether a team with the same
name already exists for the specified organization and game. If so, it
returns HTTP `409 Conflict`.

The database composite unique constraint remains the final integrity
safeguard. Application-level checks alone cannot eliminate race
conditions between simultaneous requests.

### Missing referenced records

If the supplied organization ID or game ID does not exist, the service
returns HTTP `404 Not Found`. If a non-null coach ID is supplied but
does not exist, it also returns `404 Not Found`.

## 10. Verification completed at this checkpoint

-   `.\mvnw.cmd clean compile` completed with `BUILD SUCCESS`.
-   Spring Boot started successfully.
-   MySQL connection to `esports_db` succeeded.
-   `GET /api/health` was tested successfully earlier.
-   Game API GET and POST routes were tested successfully earlier.
-   `GET /api/teams` returned the sample teams.
-   `GET /api/teams/1` returned Phoenix BGMI.
-   `POST /api/teams` created a new team and returned its generated ID.
-   The initial POST response had `createdAt: null`.
-   The service was updated to refresh the saved entity through
    `EntityManager`.
-   A second POST test confirmed that `createdAt` was populated.

## 11. Remaining work

The Team module is not complete CRUD yet. Remaining work:

1.  Implement `PUT /api/teams/{id}` to update an existing team.
2.  Implement `DELETE /api/teams/{id}` with appropriate handling for
    foreign-key restrictions and historical data.
3.  Test validation failures, missing IDs, duplicate names and optional
    coach behavior.
4.  Consider positive-value validation for IDs in request DTOs.
5.  Review error response consistency across controllers and services.
6.  Add automated tests for Game and Team modules.
7.  Continue implementing modules for players, tournaments, matches and
    analytics.
8.  Review battle-royale-specific database constraints before extending
    match workflows to other esports titles.
9.  Connect the frontend to backend APIs and replace demo data
    incrementally.

Do not assume PUT or DELETE support exists yet; they were not
implemented or tested at this checkpoint.

## 12. How another developer can continue

1.  Pull the latest repository changes.
2.  Ensure JDK 21 and MySQL 8 are installed.
3.  Create/configure the local `esports_db` database using the
    repository SQL scripts if it is not already set up.
4.  Set the local `DB_PASSWORD` environment variable.
5.  Open `Backend/esports-api`.
6.  Run `.\mvnw.cmd clean compile`.
7.  Start the server with `.\mvnw.cmd spring-boot:run`.
8.  Test the health endpoint, then the Game and Team endpoints.

Do not commit local passwords. Each developer should use their own local
database credentials.

------------------------------------------------------------------------

**End of checkpoint.** The backend currently compiles, starts, connects
to MySQL and has working Game and Team read/create endpoints. The next
planned task is Team update functionality (`PUT`), followed by delete
and additional validation/testing.
