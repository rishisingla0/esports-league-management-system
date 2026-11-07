package com.esports.api.repository;

import com.esports.api.dto.TournamentStandingResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TournamentStandingRepository {

    private final JdbcTemplate jdbcTemplate;

    public TournamentStandingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TournamentStandingResponse> getStandings(Long tournamentId) {

        String sql = """
            SELECT
                t.team_id,
                t.team_name,
                COUNT(mt.match_team_id) AS matches_played,
                COALESCE(SUM(mt.total_points), 0) AS total_points,
                AVG(mt.placement) AS average_placement,
                MIN(mt.placement) AS best_placement
            FROM tournament_registrations tr
            JOIN teams t
                ON tr.team_id = t.team_id
            LEFT JOIN matches m
                ON m.tournament_id = tr.tournament_id
                AND m.status = 'COMPLETED'
            LEFT JOIN match_teams mt
                ON mt.match_id = m.match_id
                AND mt.team_id = t.team_id
                AND mt.placement IS NOT NULL
                AND mt.total_points IS NOT NULL
            WHERE tr.tournament_id = ?
                AND tr.status = 'CONFIRMED'
            GROUP BY t.team_id, t.team_name
            ORDER BY total_points DESC,
                     best_placement ASC,
                     t.team_name ASC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new TournamentStandingResponse(
                        rs.getLong("team_id"),
                        rs.getString("team_name"),
                        rs.getLong("matches_played"),
                        rs.getBigDecimal("total_points"),
                        rs.getBigDecimal("average_placement"),
                        (Integer) rs.getObject("best_placement")
                ),
                tournamentId
        );
    }
}