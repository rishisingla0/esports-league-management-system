
USE esports_db;

-- 1. Games
INSERT INTO games (game_name, genre, developer, release_date)
VALUES
('BGMI', 'Battle Royale', 'KRAFTON', '2021-07-02'),
('Free Fire', 'Battle Royale', 'Garena', '2017-12-04');

-- 2. Organizations
INSERT INTO organizations
    (organization_name, country, founded_date, website)
VALUES
('Phoenix Esports', 'India', '2020-01-01', NULL),
('Tiger Gaming', 'India', '2021-05-01', NULL);

-- 3. Teams: two BGMI teams and one Free Fire team
INSERT INTO teams
    (organization_id, game_id, team_name)
SELECT o.organization_id, g.game_id, 'Phoenix BGMI'
FROM organizations o
JOIN games g
  ON g.game_name = 'BGMI'
WHERE o.organization_name = 'Phoenix Esports';

INSERT INTO teams
    (organization_id, game_id, team_name)
SELECT o.organization_id, g.game_id, 'Tigers BGMI'
FROM organizations o
JOIN games g
  ON g.game_name = 'BGMI'
WHERE o.organization_name = 'Tiger Gaming';

INSERT INTO teams
    (organization_id, game_id, team_name)
SELECT o.organization_id, g.game_id, 'Phoenix Free Fire'
FROM organizations o
JOIN games g
  ON g.game_name = 'Free Fire'
WHERE o.organization_name = 'Phoenix Esports';

-- 4. Players
INSERT INTO players (player_name, country, date_of_birth)
VALUES
('Aarav Sharma', 'India', '2002-01-10'),
('Vihaan Patel', 'India', '2001-04-12'),
('Arjun Singh', 'India', '2003-02-15'),
('Reyansh Kumar', 'India', '2002-07-18'),
('Aditya Verma', 'India', '2001-09-20'),
('Kabir Mehta', 'India', '2003-11-05'),
('Ishaan Gupta', 'India', '2002-03-22'),
('Rohan Malhotra', 'India', '2001-06-14'),
('Dev Shah', 'India', '2003-08-09'),
('Krish Jain', 'India', '2002-12-01');

-- 5. BGMI profiles for all ten players
INSERT INTO player_game_profiles (player_id, game_id, in_game_id)
SELECT p.player_id, g.game_id, CONCAT('BGMI_', p.player_id)
FROM players p
CROSS JOIN games g
WHERE g.game_name = 'BGMI';

-- Players 1-4 also have Free Fire profiles
INSERT INTO player_game_profiles (player_id, game_id, in_game_id)
SELECT p.player_id, g.game_id, CONCAT('FF_', p.player_id)
FROM players p
JOIN games g
  ON g.game_name = 'Free Fire'
WHERE p.player_name IN (
    'Aarav Sharma',
    'Vihaan Patel',
    'Arjun Singh',
    'Reyansh Kumar'
);

-- 6. Phoenix BGMI roster: six players, including substitutes
INSERT INTO team_rosters
    (team_id, player_id, role, start_date, status)
SELECT t.team_id, p.player_id, 'PLAYER', '2026-01-01', 'ACTIVE'
FROM teams t
CROSS JOIN players p
WHERE t.team_name = 'Phoenix BGMI'
  AND p.player_name IN (
      'Aarav Sharma',
      'Vihaan Patel',
      'Arjun Singh',
      'Reyansh Kumar',
      'Aditya Verma',
      'Kabir Mehta'
  );

-- 7. Tigers BGMI roster: four players
INSERT INTO team_rosters
    (team_id, player_id, role, start_date, status)
SELECT t.team_id, p.player_id, 'PLAYER', '2026-01-01', 'ACTIVE'
FROM teams t
CROSS JOIN players p
WHERE t.team_name = 'Tigers BGMI'
  AND p.player_name IN (
      'Ishaan Gupta',
      'Rohan Malhotra',
      'Dev Shah',
      'Krish Jain'
  );

-- Phoenix also fields a team in Free Fire
INSERT INTO team_rosters
    (team_id, player_id, role, start_date, status)
SELECT t.team_id, p.player_id, 'PLAYER', '2026-01-01', 'ACTIVE'
FROM teams t
CROSS JOIN players p
WHERE t.team_name = 'Phoenix Free Fire'
  AND p.player_name IN (
      'Aarav Sharma',
      'Vihaan Patel',
      'Arjun Singh',
      'Reyansh Kumar'
  );

-- 8. Tournaments
INSERT INTO tournaments
    (game_id, tournament_name, start_date, end_date, prize_pool, status)
SELECT game_id, 'BGMI Championship 2026',
       '2026-10-20', '2026-10-22', 100000.00, 'UPCOMING'
FROM games
WHERE game_name = 'BGMI';

INSERT INTO tournaments
    (game_id, tournament_name, start_date, end_date, prize_pool, status)
SELECT game_id, 'Free Fire Cup 2026',
       '2026-10-25', '2026-10-27', 50000.00, 'UPCOMING'
FROM games
WHERE game_name = 'Free Fire';

-- 9. Register teams for their respective tournaments
INSERT INTO tournament_registrations
    (tournament_id, team_id, registration_date, status)
SELECT t.tournament_id, tm.team_id, '2026-10-16', 'CONFIRMED'
FROM tournaments t
JOIN teams tm
  ON tm.game_id = t.game_id
WHERE t.tournament_name = 'BGMI Championship 2026'
  AND tm.team_name IN ('Phoenix BGMI', 'Tigers BGMI');

INSERT INTO tournament_registrations
    (tournament_id, team_id, registration_date, status)
SELECT t.tournament_id, tm.team_id, '2026-10-16', 'CONFIRMED'
FROM tournaments t
JOIN teams tm
  ON tm.game_id = t.game_id
WHERE t.tournament_name = 'Free Fire Cup 2026'
  AND tm.team_name = 'Phoenix Free Fire';

-- 10. Create one scheduled BGMI match
INSERT INTO matches
    (tournament_id, match_number, map_name, scheduled_at, status)
SELECT tournament_id, 1, 'Erangel',
       '2026-10-20 18:00:00', 'SCHEDULED'
FROM tournaments
WHERE tournament_name = 'BGMI Championship 2026';

-- 11. Add the two registered teams to the match
INSERT INTO match_teams (match_id, team_id)
SELECT m.match_id, t.team_id
FROM matches m
JOIN tournaments tr
  ON tr.tournament_id = m.tournament_id
JOIN teams t
  ON t.team_name IN ('Phoenix BGMI', 'Tigers BGMI')
 AND t.game_id = tr.game_id
WHERE tr.tournament_name = 'BGMI Championship 2026'
  AND m.match_number = 1;
