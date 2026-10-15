
USE esports_db;

DELIMITER $$

-- Protect roster records used by ongoing or completed matches
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
        WHERE msp.player_id IN (OLD.player_id, NEW.player_id)
          AND m.status IN ('ONGOING', 'COMPLETED')
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Roster records used in started matches cannot be changed';
    END IF;
END$$

CREATE TRIGGER trg_protect_roster_delete
BEFORE DELETE ON team_rosters
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM match_squad_players msp
        JOIN matches m
          ON m.match_id = msp.match_id
        WHERE msp.player_id = OLD.player_id
          AND m.status IN ('ONGOING', 'COMPLETED')
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Roster records used in started matches cannot be deleted';
    END IF;
END$$

-- Protect registrations for teams in ongoing or completed matches
CREATE TRIGGER trg_protect_registration_update
BEFORE UPDATE ON tournament_registrations
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM match_teams mt
        JOIN matches m
          ON m.match_id = mt.match_id
        WHERE m.tournament_id = OLD.tournament_id
          AND mt.team_id = OLD.team_id
          AND m.status IN ('ONGOING', 'COMPLETED')
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Registration used by a started match cannot be changed';
    END IF;
END$$

CREATE TRIGGER trg_protect_registration_delete
BEFORE DELETE ON tournament_registrations
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM match_teams mt
        JOIN matches m
          ON m.match_id = mt.match_id
        WHERE m.tournament_id = OLD.tournament_id
          AND mt.team_id = OLD.team_id
          AND m.status IN ('ONGOING', 'COMPLETED')
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Registration used by a started match cannot be deleted';
    END IF;
END$$

DELIMITER ;
