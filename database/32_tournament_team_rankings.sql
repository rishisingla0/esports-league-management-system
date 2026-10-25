
USE esports_db;

-- Rank teams within each tournament using completed-match results.

WITH team_results AS (
    SELECT
        tr.tournament_id,
        tr.tournament_name,
        t.team_id,
        t.team_name,
        COUNT(DISTINCT m.match_id) AS matches_played,
        SUM(
            CASE
                WHEN mt.placement = 1 THEN 1
                ELSE 0
            END
        ) AS wins,
        COALESCE(SUM(mt.total_points), 0) AS total_points
    FROM tournaments tr
    JOIN matches m
        ON m.tournament_id = tr.tournament_id
    JOIN match_teams mt
        ON mt.match_id = m.match_id
    JOIN teams t
        ON t.team_id = mt.team_id
    WHERE m.status = 'COMPLETED'
    GROUP BY
        tr.tournament_id,
        tr.tournament_name,
        t.team_id,
        t.team_name
),
team_performance AS (
    SELECT
        tr.tournament_id,
        mt.team_id,
        COALESCE(SUM(pp.kills), 0) AS total_kills,
        COALESCE(SUM(pp.assists), 0) AS total_assists,
        COALESCE(SUM(pp.damage), 0) AS total_damage
    FROM tournaments tr
    JOIN matches m
        ON m.tournament_id = tr.tournament_id
    JOIN match_teams mt
        ON mt.match_id = m.match_id
    LEFT JOIN match_squad_players msp
        ON msp.match_team_id = mt.match_team_id
       AND msp.match_id = mt.match_id
    LEFT JOIN player_performances pp
        ON pp.match_squad_player_id = msp.match_squad_player_id
    WHERE m.status = 'COMPLETED'
    GROUP BY
        tr.tournament_id,
        mt.team_id
)
SELECT
    r.tournament_name,
    r.team_name,
    r.matches_played,
    r.wins,
    r.total_points,
    p.total_kills,
    p.total_assists,
    p.total_damage,
    DENSE_RANK() OVER (
        PARTITION BY r.tournament_id
        ORDER BY
            r.total_points DESC,
            r.wins DESC,
            p.total_kills DESC,
            p.total_damage DESC
    ) AS team_rank
FROM team_results r
JOIN team_performance p
    ON p.tournament_id = r.tournament_id
   AND p.team_id = r.team_id
ORDER BY
    r.tournament_name,
    team_rank,
    r.team_name;
