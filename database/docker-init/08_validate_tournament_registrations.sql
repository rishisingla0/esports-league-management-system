
USE esports_db;

DELIMITER $$

CREATE TRIGGER trg_registration_game_insert
BEFORE INSERT ON tournament_registrations
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM tournaments AS t
        JOIN teams AS tm
          ON tm.team_id = NEW.team_id
        WHERE t.tournament_id = NEW.tournament_id
          AND t.game_id = tm.game_id
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Team and tournament must belong to the same game';
    END IF;
END$$

CREATE TRIGGER trg_registration_game_update
BEFORE UPDATE ON tournament_registrations
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM tournaments AS t
        JOIN teams AS tm
          ON tm.team_id = NEW.team_id
        WHERE t.tournament_id = NEW.tournament_id
          AND t.game_id = tm.game_id
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Team and tournament must belong to the same game';
    END IF;
END$$

DELIMITER ;
