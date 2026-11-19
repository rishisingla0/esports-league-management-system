
USE esports_db;

DELIMITER $$

-- Prevent adding teams after a match leaves SCHEDULED
CREATE TRIGGER trg_lock_match_team_insert
BEFORE INSERT ON match_teams
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM matches
        WHERE match_id = NEW.match_id
          AND status = 'SCHEDULED'
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Teams can only be added while a match is scheduled';
    END IF;
END$$

-- Prevent changing participating teams after scheduling
CREATE TRIGGER trg_lock_match_team_update
BEFORE UPDATE ON match_teams
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM matches
        WHERE match_id = OLD.match_id
          AND status = 'SCHEDULED'
    ) OR NOT EXISTS (
        SELECT 1
        FROM matches
        WHERE match_id = NEW.match_id
          AND status = 'SCHEDULED'
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Teams can only be modified while a match is scheduled';
    END IF;
END$$

-- Prevent removing participating teams after scheduling
CREATE TRIGGER trg_lock_match_team_delete
BEFORE DELETE ON match_teams
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM matches
        WHERE match_id = OLD.match_id
          AND status = 'SCHEDULED'
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Teams can only be removed while a match is scheduled';
    END IF;
END$$

DELIMITER ;
