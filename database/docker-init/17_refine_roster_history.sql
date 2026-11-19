
USE esports_db;

DROP TRIGGER IF EXISTS trg_protect_roster_update;
DROP TRIGGER IF EXISTS trg_protect_roster_delete;

DELIMITER $$

-- Prevent retroactive roster insertion for a started match
CREATE TRIGGER trg_protect_roster_insert
BEFORE INSERT ON team_rosters
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM match_squad_players msp
        JOIN match_teams mt
          ON mt.match_team_id = msp.match_team_id
         AND mt.match_id = msp.match_id
        JOIN matches m
          ON m.match_id = mt.match_id
        WHERE msp.player_id = NEW.player_id
          AND mt.team_id = NEW.team_id
          AND m.status IN ('ONGOING', 'COMPLETED')
          AND COALESCE(
              DATE(m.scheduled_at), CURRENT_DATE()
          ) >= NEW.start_date
          AND (
              NEW.end_date IS NULL
              OR COALESCE(
                  DATE(m.scheduled_at), CURRENT_DATE()
              ) <= NEW.end_date
          )
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Cannot add roster history covering a started match';
    END IF;
END$$

-- Protect roster periods used by started matches
CREATE TRIGGER trg_protect_roster_update
BEFORE UPDATE ON team_rosters
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM match_squad_players msp
        JOIN match_teams mt
          ON mt.match_team_id = msp.match_team_id
         AND mt.match_id = msp.match_id
        JOIN matches m
          ON m.match_id = mt.match_id
        WHERE msp.player_id = OLD.player_id
          AND mt.team_id = OLD.team_id
          AND m.status IN ('ONGOING', 'COMPLETED')
          AND COALESCE(
              DATE(m.scheduled_at), CURRENT_DATE()
          ) >= OLD.start_date
          AND (
              OLD.end_date IS NULL
              OR COALESCE(
                  DATE(m.scheduled_at), CURRENT_DATE()
              ) <= OLD.end_date
          )
    ) OR EXISTS (
        SELECT 1
        FROM match_squad_players msp
        JOIN match_teams mt
          ON mt.match_team_id = msp.match_team_id
         AND mt.match_id = msp.match_id
        JOIN matches m
          ON m.match_id = mt.match_id
        WHERE msp.player_id = NEW.player_id
          AND mt.team_id = NEW.team_id
          AND m.status IN ('ONGOING', 'COMPLETED')
          AND COALESCE(
              DATE(m.scheduled_at), CURRENT_DATE()
          ) >= NEW.start_date
          AND (
              NEW.end_date IS NULL
              OR COALESCE(
                  DATE(m.scheduled_at), CURRENT_DATE()
              ) <= NEW.end_date
          )
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Cannot modify roster membership covering a started match';
    END IF;
END$$

-- Protect roster records used by started matches
CREATE TRIGGER trg_protect_roster_delete
BEFORE DELETE ON team_rosters
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM match_squad_players msp
        JOIN match_teams mt
          ON mt.match_team_id = msp.match_team_id
         AND mt.match_id = msp.match_id
        JOIN matches m
          ON m.match_id = mt.match_id
        WHERE msp.player_id = OLD.player_id
          AND mt.team_id = OLD.team_id
          AND m.status IN ('ONGOING', 'COMPLETED')
          AND COALESCE(
              DATE(m.scheduled_at), CURRENT_DATE()
          ) >= OLD.start_date
          AND (
              OLD.end_date IS NULL
              OR COALESCE(
                  DATE(m.scheduled_at), CURRENT_DATE()
              ) <= OLD.end_date
          )
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Cannot delete roster membership covering a started match';
    END IF;
END$$

DELIMITER ;
