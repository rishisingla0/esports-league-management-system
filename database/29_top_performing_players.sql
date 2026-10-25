
USE esports_db;

-- Rank players by performance in completed tournament matches
SELECT
    p.player_name,
    t.team_name,
    COUNT(DISTINCT m.match_id) AS matches_played,
    COALESCE(SUM(pp.kills), 0) AS total_kills,
    COALESCE(SUM(pp.damage), 0) AS total_damage,
    COALESCE(SUM(pp.assists), 0) AS total_assists,
    RANK() OVER (
        ORDER BY
            COALESCE(SUM(pp.kills), 0) DESC,
            COALESCE(SUM(pp.damage), 0) DESC,
            COALESCE(SUM(pp.assists), 0) DESC
    ) AS player_rank
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
    player_rank,
    p.player_name;
