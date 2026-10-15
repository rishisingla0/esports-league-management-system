-- Insert Trigger
USE esports_db;

DELIMITER $$

CREATE TRIGGER trg_roster_active_game_insert
BEFORE INSERT ON team_rosters
FOR EACH ROW
BEGIN
    IF NEW.status = 'ACTIVE' AND NEW.end_date IS NULL
       AND EXISTS (
           SELECT 1
           FROM team_rosters AS existing_roster
           JOIN teams AS existing_team
             ON existing_team.team_id = existing_roster.team_id
           JOIN teams AS new_team
             ON new_team.team_id = NEW.team_id
           WHERE existing_roster.player_id = NEW.player_id
             AND existing_team.game_id = new_team.game_id
             AND existing_roster.status = 'ACTIVE'
             AND existing_roster.end_date IS NULL
       )
    THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Player already has an active roster in this game';
    END IF;
END$$

DELIMITER ;


-- Update Trigger
USE esports_db;

DELIMITER $$

CREATE TRIGGER trg_roster_active_game_update
BEFORE UPDATE ON team_rosters
FOR EACH ROW
BEGIN
    IF NEW.status = 'ACTIVE' AND NEW.end_date IS NULL
       AND EXISTS (
           SELECT 1
           FROM team_rosters AS existing_roster
           JOIN teams AS existing_team
             ON existing_team.team_id = existing_roster.team_id
           JOIN teams AS new_team
             ON new_team.team_id = NEW.team_id
           WHERE existing_roster.player_id = NEW.player_id
             AND existing_team.game_id = new_team.game_id
             AND existing_roster.status = 'ACTIVE'
             AND existing_roster.end_date IS NULL
             AND existing_roster.roster_id <> OLD.roster_id
       )
    THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Player already has an active roster in this game';
    END IF;
END$$

DELIMITER ;
