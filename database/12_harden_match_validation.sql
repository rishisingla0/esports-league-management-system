
USE esports_db;

-- Replace the existing squad eligibility trigger
DROP TRIGGER IF EXISTS trg_squad_player_eligibility_insert;

DELIMITER $$

CREATE TRIGGER trg_squad_player_eligibility_insert
BEFORE INSERT ON match_squad_players
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM match_teams mt
        JOIN teams tm
          ON tm.team_id = mt.team_id
        JOIN matches m
          ON m.match_id = mt.match_id
        JOIN team_rosters r
          ON r.team_id = tm.team_id
         AND r.player_id = NEW.player_id
        JOIN player_game_profiles pgp
          ON pgp.player_id = NEW.player_id
         AND pgp.game_id = tm.game_id
        WHERE mt.match_team_id = NEW.match_team_id
          AND mt.match_id = NEW.match_id
          AND m.status = 'SCHEDULED'
          AND r.status = 'ACTIVE'
          AND r.start_date <= COALESCE(
              DATE(m.scheduled_at), CURRENT_DATE()
          )
          AND (
              r.end_date IS NULL
              OR r.end_date >= COALESCE(
                  DATE(m.scheduled_at), CURRENT_DATE()
              )
          )
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Player must have an active eligible roster and game profile, and the match must be scheduled';
    END IF;
END$$

-- Prevent matches from bypassing squad validation at creation
CREATE TRIGGER trg_match_initial_status
BEFORE INSERT ON matches
FOR EACH ROW
BEGIN
    IF NEW.status <> 'SCHEDULED' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'A match must be created as SCHEDULED';
    END IF;
END$$

DELIMITER ;
