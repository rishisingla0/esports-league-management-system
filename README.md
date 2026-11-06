# DROPZONE — eSports Tournament Management & Analytics System

A database-driven eSports tournament management platform for organizing games, organizations, teams, players, tournaments, match participation, player performance, sponsorships, and prize distributions.

The project is being developed with a Spring Boot REST API, a MySQL relational database, and a Next.js frontend.

## Technology Stack

### Frontend
- Next.js 16
- React 19
- TypeScript
- Tailwind CSS 4

### Backend
- Java 21
- Spring Boot
- Spring Data JPA / Hibernate
- Maven
- Jakarta Bean Validation

### Database & Tools
- MySQL 8
- MySQL Workbench
- Postman
- Git and GitHub
- Visual Studio Code

## Core Features

- Game and organization management
- Team and player management
- Player game profiles
- Team rosters and player transfers
- Tournament creation and registration
- Match scheduling and team participation
- Match squad management
- Player performance records
- Coach management
- Sponsor and team sponsorship management
- Tournament prize distributions

## Backend Development Status

The core CRUD REST API modules have been implemented and tested through Postman.

| # | Module | Status |
|---:|---|---|
| 1 | Games | Completed |
| 2 | Organizations | Completed |
| 3 | Teams | Completed |
| 4 | Players | Completed |
| 5 | Player Game Profiles | Completed |
| 6 | Tournaments | Completed |
| 7 | Tournament Registrations | Completed |
| 8 | Matches | Completed |
| 9 | Match Teams | Completed |
| 10 | Match Squad Players | Completed |
| 11 | Player Performances | Completed |
| 12 | Coaches | Completed |
| 13 | Team Rosters | Completed |
| 14 | Player Transfers | Completed |
| 15 | Sponsors | Completed |
| 16 | Team Sponsorships | Completed |
| 17 | Prize Distributions | Completed |

API testing has covered CRUD operations, request validation, foreign-key constraints, uniqueness constraints, and not-found responses where applicable.

## Planned Next Steps

- Dashboard summary and statistics APIs
- Player and team leaderboards
- Tournament standings and analytics
- Connect the Next.js frontend to the Spring Boot APIs
- End-to-end testing and UI polish

## Database

The project uses a local MySQL database named `esports_db` during development. Database schema, constraints, and related SQL scripts are maintained separately from the application code.

## Backend Setup

1. Install Java 21 and MySQL 8.
2. Create/configure the `esports_db` database using the project's SQL scripts.
3. Open the backend Spring Boot project.
4. Configure the database connection using the environment variable `DB_PASSWORD`.
5. From the backend project directory, run:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

The API is available locally at:

```text
http://localhost:8080
```

## API Base Path

```text
/api
```

Use Postman to test the available endpoints.

## Work Logs

Development progress is documented in [`docs/work-logs/`](docs/work-logs/).
