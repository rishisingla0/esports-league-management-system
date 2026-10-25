
USE esports_db;

-- Match results report for a tournament
SELECT
    tr.tournament_name,
    m.match_number,
    m.map_name,
    m.scheduled_at,
    t.team_name,
    mt.placement,
    mt.total_points,
    COUNT(DISTINCT msp.player_id) AS selected_players,
    COALESCE(SUM(pp.kills), 0) AS team_kills,
    COALESCE(SUM(pp.assists), 0) AS team_assists,
    COALESCE(SUM(pp.damage), 0) AS team_damage
FROM matches m
JOIN tournaments tr
    ON tr.tournament_id = m.tournament_id
JOIN match_teams mt
    ON mt.match_id = m.match_id
JOIN teams t
    ON t.team_id = mt.team_id
LEFT JOIN match_squad_players msp
    ON msp.match_team_id = mt.match_team_id
   AND msp.match_id = mt.match_id
LEFT JOIN player_performances pp
    ON pp.match_squad_player_id = msp.match_squad_player_id
WHERE m.status = 'COMPLETED'
  AND tr.tournament_name = 'BGMI Championship 2026'
GROUP BY
    tr.tournament_name,
    m.match_id,
    m.match_number,
    m.map_name,
    m.scheduled_at,
    t.team_id,
    t.team_name,
    mt.placement,
    mt.total_points
ORDER BY
    m.match_number,
    mt.placement;
