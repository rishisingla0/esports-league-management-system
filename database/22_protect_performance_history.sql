
USE esports_db;

DELIMITER //

CREATE TRIGGER trg_validate_performance_update
BEFORE UPDATE ON player_performances
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM match_squad_players msp
        JOIN matches m
            ON m.match_id = msp.match_id
        WHERE msp.match_squad_player_id =
              OLD.match_squad_player_id
          AND m.status = 'ONGOING'
    ) OR NOT EXISTS (
        SELECT 1
        FROM match_squad_players msp
        JOIN matches m
            ON m.match_id = msp.match_id
        WHERE msp.match_squad_player_id =
              NEW.match_squad_player_id
          AND m.status = 'ONGOING'
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Performance records can only be updated while the match is ongoing';
    END IF;
END//

CREATE TRIGGER trg_validate_performance_delete
BEFORE DELETE ON player_performances
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM match_squad_players msp
        JOIN matches m
            ON m.match_id = msp.match_id
        WHERE msp.match_squad_player_id =
              OLD.match_squad_player_id
          AND m.status = 'ONGOING'
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Performance records can only be deleted while the match is ongoing';
    END IF;
END//

DELIMITER ;
