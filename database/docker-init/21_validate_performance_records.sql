
USE esports_db;

DELIMITER //

CREATE TRIGGER trg_validate_performance_insert
BEFORE INSERT ON player_performances
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM match_squad_players msp
        JOIN matches m
            ON m.match_id = msp.match_id
        WHERE msp.match_squad_player_id =
              NEW.match_squad_player_id
          AND m.status IN ('ONGOING', 'COMPLETED')
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Performance records can only be inserted for ongoing or completed matches';
    END IF;
END//

DELIMITER ;
