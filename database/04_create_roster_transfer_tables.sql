
USE esports_db;

-- 7. Player membership history
CREATE TABLE team_rosters (
    roster_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    team_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    role VARCHAR(50),
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT fk_rosters_team
        FOREIGN KEY (team_id)
        REFERENCES teams(team_id),

    CONSTRAINT fk_rosters_player
        FOREIGN KEY (player_id)
        REFERENCES players(player_id),

    CONSTRAINT chk_roster_dates
        CHECK (end_date IS NULL OR end_date >= start_date),

    CONSTRAINT chk_roster_status
        CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

-- 8. Player transfer history
CREATE TABLE player_transfers (
    transfer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    player_id BIGINT NOT NULL,
    from_team_id BIGINT NOT NULL,
    to_team_id BIGINT NOT NULL,
    transfer_date DATE NOT NULL,
    transfer_fee DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    contract_months INT,

    CONSTRAINT fk_transfers_player
        FOREIGN KEY (player_id)
        REFERENCES players(player_id),

    CONSTRAINT fk_transfers_from_team
        FOREIGN KEY (from_team_id)
        REFERENCES teams(team_id),

    CONSTRAINT fk_transfers_to_team
        FOREIGN KEY (to_team_id)
        REFERENCES teams(team_id),

    CONSTRAINT chk_transfer_teams
        CHECK (from_team_id <> to_team_id),

    CONSTRAINT chk_transfer_fee
        CHECK (transfer_fee >= 0),

    CONSTRAINT chk_contract_months
        CHECK (contract_months IS NULL OR contract_months > 0)
);
