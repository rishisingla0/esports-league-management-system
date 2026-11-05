# Day 2 --- Backend API Development

**Project:** Esports Tournament Management & Analytics System\
**Backend:** Spring Boot, Java 21, Maven, MySQL 8\
**Branch:** `rohan`

## 1. Day 2 Objectives

Continue building the Spring Boot REST API by implementing CRUD
operations for core esports entities, validating requests against the
existing MySQL schema, and testing endpoints through Postman.

## 2. Work Completed

### A. Teams API --- CRUD completion

Completed the Teams API with create, retrieve-all, retrieve-by-ID,
update, and delete operations. Added DTO-based request/response handling
and validation of related organization, game, and coach records.

### B. Players API

Implemented CRUD endpoints:

-   `GET /api/players`
-   `GET /api/players/{id}`
-   `POST /api/players`
-   `PUT /api/players/{id}`
-   `DELETE /api/players/{id}`

Added the Player entity, repository, request/response DTOs, service, and
controller.

### C. Player Game Profiles API

Implemented CRUD endpoints:

-   `GET /api/player-game-profiles`
-   `GET /api/player-game-profiles/{id}`
-   `POST /api/player-game-profiles`
-   `PUT /api/player-game-profiles/{id}`
-   `DELETE /api/player-game-profiles/{id}`

Added validation for existing player/game records, one profile per
player for a given game, and unique in-game IDs within the same game.
Tested creation, duplicate rejection, update, deletion, and retrieval of
a deleted profile in Postman.

### D. Tournaments API

Implemented CRUD endpoints:

-   `GET /api/tournaments`
-   `GET /api/tournaments/{id}`
-   `POST /api/tournaments`
-   `PUT /api/tournaments/{id}`
-   `DELETE /api/tournaments/{id}`

Added validation for existing games, tournament dates, non-negative
prize pools, and allowed statuses: `UPCOMING`, `ONGOING`, `COMPLETED`,
and `CANCELLED`.

Tested successful creation and update, invalid date rejection, deletion,
and retrieval after deletion.

### E. Tournament Registrations API

Implemented CRUD endpoints:

-   `GET /api/tournament-registrations`
-   `GET /api/tournament-registrations/{id}`
-   `POST /api/tournament-registrations`
-   `PUT /api/tournament-registrations/{id}`
-   `DELETE /api/tournament-registrations/{id}`

Added the entity, repository, request/response DTOs, service, and
controller.

Registration fields include tournament, team, registration date,
optional seed, and status (`REGISTERED`, `CONFIRMED`, or `WITHDRAWN`).

Added application-level validation for related records, matching games,
duplicate registrations, positive seed values, and valid statuses.

The existing MySQL triggers enforce additional database rules:

-   A team and tournament must belong to the same game.
-   A team cannot be registered retroactively after a match has started.
-   Registration relationships used by started matches cannot be
    modified.
-   Registrations used by started matches cannot be deleted.

## 3. Postman Testing --- Tournament Registrations

All six verification checks passed:

  Test                              Expected HTTP Status Result
  ------------------------------- ---------------------- --------
  Get all registrations                           200 OK Passed
  Create registration                        201 Created Passed
  Duplicate registration                    409 Conflict Passed
  Update registration                             200 OK Passed
  Delete registration                     204 No Content Passed
  Retrieve deleted registration            404 Not Found Passed

A first registration attempt used a Free Fire team with a BGMI
tournament. The database correctly rejected it because the game IDs did
not match. Retrying with the Free Fire tournament succeeded. The
temporary test registration was deleted after testing.

## 4. Technical Concepts Used

-   Spring Boot REST controllers
-   Service and repository layers
-   Spring Data JPA
-   Entity relationships using `@ManyToOne`
-   DTOs using Java records
-   Jakarta Bean Validation
-   Dependency injection
-   `ResponseEntity` and HTTP status codes
-   Transaction management
-   MySQL foreign keys, unique constraints, check constraints, and
    triggers
-   Postman API testing

## 5. Current Backend Progress

The following API modules have been implemented:

1.  Games API
2.  Organizations API
3.  Teams API
4.  Players API
5.  Player Game Profiles API
6.  Tournaments API
7.  Tournament Registrations API

The Tournament Registrations API compiled successfully, passed its
Postman tests, and was committed and pushed to the `rohan` branch.

## 6. Next --- Day 3

Planned next module: **Matches API**.

Before implementation, inspect the exact MySQL table definitions and
related triggers for:

-   `matches`
-   `match_teams`

Design the API around the existing database constraints and
relationships. Do not assume the schema; inspect it first.

Potential operations to plan after schema inspection include retrieving
matches, retrieving one match, creating a match, updating match
details/status, and deleting a match subject to database protections.

Further match-related modules, such as match teams, squads, and player
performances, can be planned after the core Matches API.

------------------------------------------------------------------------

**Day 2 status: Completed.**
