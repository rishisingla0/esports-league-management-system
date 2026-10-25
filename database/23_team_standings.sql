
USE esports_db;

-- Team standings for a tournament
SELECT
    t.team_name,
    COUNT(mt.match_team_id) AS matches_played,
    SUM(
        CASE
            WHEN mt.placement = 1 THEN 1
            ELSE 0
        END
    ) AS first_place_finishes,
    COALESCE(SUM(mt.total_points), 0) AS total_points,
    ROUND(AVG(mt.placement), 2) AS average_placement
FROM teams t
JOIN tournament_registrations reg
    ON reg.team_id = t.team_id
JOIN tournaments tr
    ON tr.tournament_id = reg.tournament_id
JOIN matches m
    ON m.tournament_id = tr.tournament_id
   AND m.status = 'COMPLETED'
JOIN match_teams mt
    ON mt.match_id = m.match_id
   AND mt.team_id = t.team_id
WHERE tr.tournament_name = 'BGMI Championship 2026'
GROUP BY
    t.team_id,
    t.team_name
ORDER BY
    total_points DESC,
    first_place_finishes DESC,
    average_placement ASC;
