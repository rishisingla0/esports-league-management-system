
USE esports_db;

DROP TRIGGER IF EXISTS trg_match_four_players_before_start;

DELIMITER $$

CREATE TRIGGER trg_match_four_players_before_start
BEFORE UPDATE ON matches
FOR EACH ROW
BEGIN
    -- Prevent reopening completed or cancelled matches
    IF OLD.status IN ('COMPLETED', 'CANCELLED')
       AND NEW.status <> OLD.status THEN

        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Completed or cancelled matches cannot be reopened';
    END IF;

    -- Validate squads before a match starts or completes
    IF NEW.status IN ('ONGOING', 'COMPLETED')
       AND OLD.status <> NEW.status THEN

        IF NOT EXISTS (
            SELECT 1
            FROM match_teams
            WHERE match_id = NEW.match_id
        ) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT =
                    'A match must have participating teams';
        END IF;

        IF EXISTS (
            SELECT mt.match_team_id
            FROM match_teams mt
            LEFT JOIN match_squad_players msp
              ON msp.match_team_id = mt.match_team_id
             AND msp.match_id = mt.match_id
            WHERE mt.match_id = NEW.match_id
            GROUP BY mt.match_team_id
            HAVING COUNT(msp.match_squad_player_id) <> 4
        ) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT =
                    'Every participating team must have exactly four players';
        END IF;
    END IF;
END$$

DELIMITER ;
