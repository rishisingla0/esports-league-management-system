
USE esports_db;

-- Analyze overall performance across maps in a tournament
SELECT
    m.map_name,
    COUNT(DISTINCT m.match_id) AS completed_matches,
    COUNT(DISTINCT mt.team_id) AS participating_teams,
    COALESCE(SUM(pp.kills), 0) AS total_kills,
    COALESCE(SUM(pp.assists), 0) AS total_assists,
    COALESCE(SUM(pp.damage), 0) AS total_damage,
    ROUND(
        COALESCE(SUM(pp.kills), 0) /
        NULLIF(COUNT(DISTINCT m.match_id), 0),
        2
    ) AS average_kills_per_match
FROM matches m
JOIN tournaments tr
    ON tr.tournament_id = m.tournament_id
LEFT JOIN match_teams mt
    ON mt.match_id = m.match_id
LEFT JOIN match_squad_players msp
    ON msp.match_team_id = mt.match_team_id
   AND msp.match_id = mt.match_id
LEFT JOIN player_performances pp
    ON pp.match_squad_player_id = msp.match_squad_player_id
WHERE tr.tournament_name = 'BGMI Championship 2026'
  AND m.status = 'COMPLETED'
GROUP BY m.map_name
ORDER BY total_kills DESC, m.map_name;
