
USE esports_db;

-- Companies and brands that sponsor esports teams
CREATE TABLE sponsors (
    sponsor_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sponsor_name VARCHAR(150) NOT NULL UNIQUE,
    industry VARCHAR(100),
    country VARCHAR(100),
    website VARCHAR(255)
);

-- Many-to-many relationship between teams and sponsors
CREATE TABLE team_sponsorships (
    sponsorship_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    team_id BIGINT NOT NULL,
    sponsor_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    sponsorship_amount DECIMAL(14, 2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT fk_sponsorships_team
        FOREIGN KEY (team_id)
        REFERENCES teams(team_id),

    CONSTRAINT fk_sponsorships_sponsor
        FOREIGN KEY (sponsor_id)
        REFERENCES sponsors(sponsor_id),

    CONSTRAINT chk_sponsorship_dates
        CHECK (end_date IS NULL OR end_date >= start_date),

    CONSTRAINT chk_sponsorship_amount
        CHECK (sponsorship_amount >= 0),

    CONSTRAINT chk_sponsorship_status
        CHECK (status IN ('ACTIVE', 'COMPLETED', 'CANCELLED'))
);

-- Prize money awarded to teams in tournaments
CREATE TABLE prize_distributions (
    prize_distribution_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tournament_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    placement INT NOT NULL,
    prize_amount DECIMAL(14, 2) NOT NULL DEFAULT 0.00,
    awarded_at DATE,

    CONSTRAINT fk_prizes_tournament
        FOREIGN KEY (tournament_id)
        REFERENCES tournaments(tournament_id),

    CONSTRAINT fk_prizes_team
        FOREIGN KEY (team_id)
        REFERENCES teams(team_id),

    CONSTRAINT uq_tournament_prize_team
        UNIQUE (tournament_id, team_id),

    CONSTRAINT uq_tournament_prize_placement
        UNIQUE (tournament_id, placement),

    CONSTRAINT chk_prize_placement
        CHECK (placement > 0),

    CONSTRAINT chk_prize_amount
        CHECK (prize_amount >= 0)
);
