
USE esports_db;

-- 1. Games supported by the platform
CREATE TABLE games (
    game_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    game_name VARCHAR(100) NOT NULL UNIQUE,
    genre VARCHAR(50) NOT NULL,
    developer VARCHAR(100) NOT NULL,
    release_date DATE
);

-- 2. eSports organizations
CREATE TABLE organizations (
    organization_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    organization_name VARCHAR(150) NOT NULL UNIQUE,
    country VARCHAR(100),
    founded_date DATE,
    website VARCHAR(255)
);

-- 3. Coaches
CREATE TABLE coaches (
    coach_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    coach_name VARCHAR(100) NOT NULL,
    country VARCHAR(100),
    hire_date DATE
);

-- 4. Game-specific competitive teams
CREATE TABLE teams (
    team_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    organization_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    coach_id BIGINT NULL,
    team_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_teams_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations(organization_id),

    CONSTRAINT fk_teams_game
        FOREIGN KEY (game_id)
        REFERENCES games(game_id),

    CONSTRAINT fk_teams_coach
        FOREIGN KEY (coach_id)
        REFERENCES coaches(coach_id),

    CONSTRAINT uq_team_name_per_game_org
        UNIQUE (organization_id, game_id, team_name)
);
