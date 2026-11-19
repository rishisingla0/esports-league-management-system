
USE esports_db;

-- One record represents one battle royale round
CREATE TABLE matches (
    match_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tournament_id BIGINT NOT NULL,
    match_number INT NOT NULL,
    map_name VARCHAR(100) NOT NULL,
    scheduled_at DATETIME,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',

    CONSTRAINT fk_matches_tournament
        FOREIGN KEY (tournament_id)
        REFERENCES tournaments(tournament_id),

    CONSTRAINT uq_tournament_match_number
        UNIQUE (tournament_id, match_number),

    CONSTRAINT chk_match_number
        CHECK (match_number > 0),

    CONSTRAINT chk_match_status
        CHECK (
            status IN (
                'SCHEDULED',
                'ONGOING',
                'COMPLETED',
                'CANCELLED'
            )
        )
);

-- One record per team participating in a match
CREATE TABLE match_teams (
    match_team_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    match_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    placement INT,
    total_points DECIMAL(8, 2),

    CONSTRAINT fk_match_teams_match
        FOREIGN KEY (match_id)
        REFERENCES matches(match_id),

    CONSTRAINT fk_match_teams_team
        FOREIGN KEY (team_id)
        REFERENCES teams(team_id),

    CONSTRAINT uq_team_per_match
        UNIQUE (match_id, team_id),

    CONSTRAINT uq_match_team_reference
        UNIQUE (match_team_id, match_id),

    CONSTRAINT chk_placement
        CHECK (placement IS NULL OR placement > 0),

    CONSTRAINT chk_total_points
        CHECK (
            total_points IS NULL OR total_points >= 0
        )
);
