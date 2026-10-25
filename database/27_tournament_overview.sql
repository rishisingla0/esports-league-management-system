
USE esports_db;

-- Tournament overview report with independent aggregations
SELECT
    tr.tournament_name,
    g.game_name,
    COALESCE(ms.completed_matches, 0) AS completed_matches,
    COALESCE(ms.participating_teams, 0) AS participating_teams,
    COALESCE(ps.unique_players, 0) AS unique_players,
    COALESCE(ps.total_player_kills, 0) AS total_player_kills,
    COALESCE(ps.total_player_damage, 0) AS total_player_damage,
    COALESCE(ms.total_team_points, 0) AS total_team_points
FROM tournaments tr
JOIN games g
    ON g.game_id = tr.game_id
LEFT JOIN (
    SELECT
        m.tournament_id,
        COUNT(DISTINCT m.match_id) AS completed_matches,
        COUNT(DISTINCT mt.team_id) AS participating_teams,
        COALESCE(SUM(mt.total_points), 0) AS total_team_points
    FROM matches m
    LEFT JOIN match_teams mt
        ON mt.match_id = m.match_id
    WHERE m.status = 'COMPLETED'
    GROUP BY m.tournament_id
) ms
    ON ms.tournament_id = tr.tournament_id
LEFT JOIN (
    SELECT
        m.tournament_id,
        COUNT(DISTINCT msp.player_id) AS unique_players,
        COALESCE(SUM(pp.kills), 0) AS total_player_kills,
        COALESCE(SUM(pp.damage), 0) AS total_player_damage
    FROM matches m
    JOIN match_teams mt
        ON mt.match_id = m.match_id
    JOIN match_squad_players msp
        ON msp.match_team_id = mt.match_team_id
       AND msp.match_id = mt.match_id
    LEFT JOIN player_performances pp
        ON pp.match_squad_player_id = msp.match_squad_player_id
    WHERE m.status = 'COMPLETED'
    GROUP BY m.tournament_id
) ps
    ON ps.tournament_id = tr.tournament_id
WHERE tr.tournament_name = 'BGMI Championship 2026';
