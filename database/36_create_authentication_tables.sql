
USE esports_db;

-- ==========================================
-- DROPZONE AUTHENTICATION TABLES
-- ==========================================

-- 1. USERS
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,

    full_name VARCHAR(100) NOT NULL,

    email VARCHAR(150) NOT NULL UNIQUE,

    password_hash VARCHAR(255) NOT NULL,

    role VARCHAR(30) NOT NULL DEFAULT 'TEAM_MANAGER',

    enabled BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_user_role
        CHECK (role IN ('ORGANIZER', 'TEAM_MANAGER'))
);


-- 2. TEAM MANAGER PROFILES
CREATE TABLE team_manager_profiles (
    user_id BIGINT PRIMARY KEY,

    team_id BIGINT NOT NULL UNIQUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_manager_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_manager_team
        FOREIGN KEY (team_id)
        REFERENCES teams(team_id)
        ON DELETE RESTRICT
);
