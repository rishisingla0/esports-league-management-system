
USE esports_db;

-- Player consistency analysis across completed matches
SELECT
    p.player_name,
    t.team_name,
    COUNT(DISTINCT m.match_id) AS matches_played,
    COALESCE(SUM(pp.kills), 0) AS total_kills,
    ROUND(
        COALESCE(SUM(pp.kills), 0)
        / NULLIF(COUNT(DISTINCT m.match_id), 0),
        2
    ) AS average_kills_per_match,
    COALESCE(SUM(pp.damage), 0) AS total_damage,
    ROUND(
        COALESCE(SUM(pp.damage), 0)
        / NULLIF(COUNT(DISTINCT m.match_id), 0),
        2
    ) AS average_damage_per_match
FROM players p
JOIN match_squad_players msp
    ON msp.player_id = p.player_id
JOIN match_teams mt
    ON mt.match_team_id = msp.match_team_id
   AND mt.match_id = msp.match_id
JOIN teams t
    ON t.team_id = mt.team_id
JOIN matches m
    ON m.match_id = msp.match_id
JOIN tournaments tr
    ON tr.tournament_id = m.tournament_id
LEFT JOIN player_performances pp
    ON pp.match_squad_player_id = msp.match_squad_player_id
WHERE tr.tournament_name = 'BGMI Championship 2026'
  AND m.status = 'COMPLETED'
GROUP BY
    p.player_id,
    p.player_name,
    t.team_id,
    t.team_name
ORDER BY
    average_kills_per_match DESC,
    average_damage_per_match DESC;
