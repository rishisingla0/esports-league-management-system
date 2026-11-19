
USE esports_db;

-- 9. Tournament details
CREATE TABLE tournaments (
    tournament_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    game_id BIGINT NOT NULL,
    tournament_name VARCHAR(150) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    prize_pool DECIMAL(14, 2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'UPCOMING',

    CONSTRAINT fk_tournaments_game
        FOREIGN KEY (game_id)
        REFERENCES games(game_id),

    CONSTRAINT chk_tournament_dates
        CHECK (end_date >= start_date),

    CONSTRAINT chk_tournament_prize
        CHECK (prize_pool >= 0),

    CONSTRAINT chk_tournament_status
        CHECK (status IN ('UPCOMING', 'ONGOING', 'COMPLETED', 'CANCELLED'))
);

-- 10. Teams registered for tournaments
CREATE TABLE tournament_registrations (
    registration_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tournament_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    registration_date DATE NOT NULL,
    seed INT,
    status VARCHAR(20) NOT NULL DEFAULT 'REGISTERED',

    CONSTRAINT fk_registrations_tournament
        FOREIGN KEY (tournament_id)
        REFERENCES tournaments(tournament_id),

    CONSTRAINT fk_registrations_team
        FOREIGN KEY (team_id)
        REFERENCES teams(team_id),

    CONSTRAINT uq_tournament_team
        UNIQUE (tournament_id, team_id),

    CONSTRAINT chk_registration_seed
        CHECK (seed IS NULL OR seed > 0),

    CONSTRAINT chk_registration_status
        CHECK (status IN ('REGISTERED', 'CONFIRMED', 'WITHDRAWN'))
);
