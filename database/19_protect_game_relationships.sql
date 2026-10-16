
USE esports_db;

DELIMITER $$

-- Prevent changing a team's game after registration or match participation
CREATE TRIGGER trg_protect_team_game_update
BEFORE UPDATE ON teams
FOR EACH ROW
BEGIN
    IF NEW.game_id <> OLD.game_id
       AND (
           EXISTS (
               SELECT 1
               FROM tournament_registrations tr
               WHERE tr.team_id = OLD.team_id
           )
           OR EXISTS (
               SELECT 1
               FROM match_teams mt
               WHERE mt.team_id = OLD.team_id
           )
       )
    THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Cannot change a team game after registration or match participation';
    END IF;
END$$

-- Prevent changing a tournament's game after registrations or matches exist
CREATE TRIGGER trg_protect_tournament_game_update
BEFORE UPDATE ON tournaments
FOR EACH ROW
BEGIN
    IF NEW.game_id <> OLD.game_id
       AND (
           EXISTS (
               SELECT 1
               FROM tournament_registrations tr
               WHERE tr.tournament_id = OLD.tournament_id
           )
           OR EXISTS (
               SELECT 1
               FROM matches m
               WHERE m.tournament_id = OLD.tournament_id
           )
       )
    THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Cannot change a tournament game after registrations or matches exist';
    END IF;
END$$

DELIMITER ;
