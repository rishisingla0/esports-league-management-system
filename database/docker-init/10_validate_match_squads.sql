
USE esports_db;

DELIMITER $$

-- Validate team eligibility when adding a team to a match
CREATE TRIGGER trg_match_team_eligibility_insert
BEFORE INSERT ON match_teams
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM matches m
        JOIN tournaments t
          ON t.tournament_id = m.tournament_id
        JOIN teams tm
          ON tm.team_id = NEW.team_id
         AND tm.game_id = t.game_id
        JOIN tournament_registrations tr
          ON tr.tournament_id = t.tournament_id
         AND tr.team_id = tm.team_id
        WHERE m.match_id = NEW.match_id
          AND tr.status IN ('REGISTERED', 'CONFIRMED')
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Team must be registered for a tournament of the same game';
    END IF;
END$$

-- Validate team eligibility when changing match participation
CREATE TRIGGER trg_match_team_eligibility_update
BEFORE UPDATE ON match_teams
FOR EACH ROW
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM matches m
        JOIN tournaments t
          ON t.tournament_id = m.tournament_id
        JOIN teams tm
          ON tm.team_id = NEW.team_id
         AND tm.game_id = t.game_id
        JOIN tournament_registrations tr
          ON tr.tournament_id = t.tournament_id
         AND tr.team_id = tm.team_id
        WHERE m.match_id = NEW.match_id
          AND tr.status IN ('REGISTERED', 'CONFIRMED')
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Team must be registered for a tournament of the same game';
    END IF;
END$$

-- Validate that a selected player belongs to the team's roster
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
                'Player must have an eligible roster record and game profile';
    END IF;
END$$

-- Enforce exactly four selected players before a match starts or completes
CREATE TRIGGER trg_match_four_players_before_start
BEFORE UPDATE ON matches
FOR EACH ROW
BEGIN
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
