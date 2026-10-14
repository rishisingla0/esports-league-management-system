
USE esports_db;

-- 5. Permanent player identity
CREATE TABLE players (
    player_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    player_name VARCHAR(100) NOT NULL,
    country VARCHAR(100),
    date_of_birth DATE
);

-- 6. Game-specific player identities
CREATE TABLE player_game_profiles (
    profile_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    player_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    in_game_id VARCHAR(100) NOT NULL,

    CONSTRAINT fk_profiles_player
        FOREIGN KEY (player_id)
        REFERENCES players(player_id),

    CONSTRAINT fk_profiles_game
        FOREIGN KEY (game_id)
        REFERENCES games(game_id),

    CONSTRAINT uq_profile_game_ign
        UNIQUE (game_id, in_game_id),

    CONSTRAINT uq_player_game
        UNIQUE (player_id, game_id)
);
