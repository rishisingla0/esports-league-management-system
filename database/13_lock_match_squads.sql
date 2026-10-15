
USE esports_db;

DELIMITER $$

-- Prevent adding players after a match is scheduled
CREATE TRIGGER trg_lock_squad_insert
BEFORE INSERT ON match_squad_players
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
                'Squads can only be modified while a match is scheduled';
    END IF;
END$$

-- Prevent changing squad members after a match is scheduled
CREATE TRIGGER trg_lock_squad_update
BEFORE UPDATE ON match_squad_players
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
                'Squads can only be modified while a match is scheduled';
    END IF;
END$$

-- Prevent removing squad members after a match is scheduled
CREATE TRIGGER trg_lock_squad_delete
BEFORE DELETE ON match_squad_players
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
                'Squads can only be modified while a match is scheduled';
    END IF;
END$$

DELIMITER ;
