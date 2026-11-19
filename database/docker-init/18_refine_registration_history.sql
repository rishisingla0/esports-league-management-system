
USE esports_db;

DROP TRIGGER IF EXISTS trg_protect_registration_update;

DELIMITER $$

-- Prevent retroactive registrations for started matches
CREATE TRIGGER trg_protect_registration_insert
BEFORE INSERT ON tournament_registrations
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM matches m
        JOIN match_teams mt
          ON mt.match_id = m.match_id
        WHERE m.tournament_id = NEW.tournament_id
          AND mt.team_id = NEW.team_id
          AND m.status IN ('ONGOING', 'COMPLETED')
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Cannot register a team retroactively after a match has started';
    END IF;
END$$

-- Protect both the old and new registration relationships
CREATE TRIGGER trg_protect_registration_update
BEFORE UPDATE ON tournament_registrations
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM matches m
        JOIN match_teams mt
          ON mt.match_id = m.match_id
        WHERE m.tournament_id = OLD.tournament_id
          AND mt.team_id = OLD.team_id
          AND m.status IN ('ONGOING', 'COMPLETED')
    ) OR EXISTS (
        SELECT 1
        FROM matches m
        JOIN match_teams mt
          ON mt.match_id = m.match_id
        WHERE m.tournament_id = NEW.tournament_id
          AND mt.team_id = NEW.team_id
          AND m.status IN ('ONGOING', 'COMPLETED')
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Cannot modify registration relationships used by started matches';
    END IF;
END$$

DELIMITER ;
