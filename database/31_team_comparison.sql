
USE esports_db;

-- Compare team results and player performance
-- in BGMI Championship 2026

WITH team_results AS (
    SELECT
        mt.team_id,
        COUNT(DISTINCT m.match_id) AS matches_played,
        SUM(
            CASE
                WHEN mt.placement = 1 THEN 1
                ELSE 0
            END
        ) AS wins,
        COALESCE(SUM(mt.total_points), 0) AS total_points
    FROM match_teams mt
    JOIN matches m
        ON m.match_id = mt.match_id
    JOIN tournaments tr
        ON tr.tournament_id = m.tournament_id
    WHERE tr.tournament_name = 'BGMI Championship 2026'
      AND m.status = 'COMPLETED'
    GROUP BY mt.team_id
),
team_performance AS (
    SELECT
        mt.team_id,
        COALESCE(SUM(pp.kills), 0) AS total_kills,
        COALESCE(SUM(pp.assists), 0) AS total_assists,
        COALESCE(SUM(pp.damage), 0) AS total_damage
    FROM match_teams mt
    JOIN matches m
        ON m.match_id = mt.match_id
    JOIN tournaments tr
        ON tr.tournament_id = m.tournament_id
    LEFT JOIN match_squad_players msp
        ON msp.match_team_id = mt.match_team_id
       AND msp.match_id = mt.match_id
    LEFT JOIN player_performances pp
        ON pp.match_squad_player_id = msp.match_squad_player_id
    WHERE tr.tournament_name = 'BGMI Championship 2026'
      AND m.status = 'COMPLETED'
    GROUP BY mt.team_id
)
SELECT
    t.team_name,
    COALESCE(r.matches_played, 0) AS matches_played,
    COALESCE(r.wins, 0) AS wins,
    COALESCE(r.total_points, 0) AS total_points,
    COALESCE(p.total_kills, 0) AS total_kills,
    COALESCE(p.total_assists, 0) AS total_assists,
    COALESCE(p.total_damage, 0) AS total_damage
FROM team_results r
JOIN teams t
    ON t.team_id = r.team_id
LEFT JOIN team_performance p
    ON p.team_id = r.team_id
ORDER BY
    wins DESC,
    total_points DESC,
    total_kills DESC,
    total_damage DESC;
