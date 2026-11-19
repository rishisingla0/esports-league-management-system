
USE esports_db;

-- Players selected to represent a team in a particular match
CREATE TABLE match_squad_players (
    match_squad_player_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    match_team_id BIGINT NOT NULL,
    match_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,

    CONSTRAINT fk_squad_match_team
        FOREIGN KEY (match_team_id, match_id)
        REFERENCES match_teams(match_team_id, match_id),

    CONSTRAINT fk_squad_player
        FOREIGN KEY (player_id)
        REFERENCES players(player_id),

    -- A player cannot represent two teams in the same match
    CONSTRAINT uq_player_once_per_match
        UNIQUE (match_id, player_id),

    CONSTRAINT uq_player_once_per_squad
        UNIQUE (match_team_id, player_id)
);

-- Individual performance statistics for each selected player
CREATE TABLE player_performances (
    performance_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    match_squad_player_id BIGINT NOT NULL UNIQUE,
    kills INT NOT NULL DEFAULT 0,
    damage INT NOT NULL DEFAULT 0,
    survival_time_seconds INT,
    assists INT NOT NULL DEFAULT 0,

    CONSTRAINT fk_performance_squad_player
        FOREIGN KEY (match_squad_player_id)
        REFERENCES match_squad_players(match_squad_player_id),

    CONSTRAINT chk_performance_kills
        CHECK (kills >= 0),

    CONSTRAINT chk_performance_damage
        CHECK (damage >= 0),

    CONSTRAINT chk_performance_survival
        CHECK (
            survival_time_seconds IS NULL
            OR survival_time_seconds >= 0
        ),

    CONSTRAINT chk_performance_assists
        CHECK (assists >= 0)
);
