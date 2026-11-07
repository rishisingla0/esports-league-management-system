package com.esports.api.repository;

import com.esports.api.dto.TournamentOverviewResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TournamentAnalyticsRepository {

    private final JdbcTemplate jdbcTemplate;

    public TournamentAnalyticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TournamentOverviewResponse> getTournamentOverview() {

        String sql = """
            SELECT
                t.tournament_id,
                t.game_id,
                t.tournament_name,
                t.start_date,
                t.end_date,
                t.prize_pool,
                t.status,
                COUNT(DISTINCT tr.team_id) AS registered_teams,
                COUNT(DISTINCT m.match_id) AS total_matches,
                COUNT(DISTINCT CASE
                    WHEN m.status = 'COMPLETED' THEN m.match_id
                END) AS completed_matches
            FROM tournaments t
            LEFT JOIN tournament_registrations tr
                ON t.tournament_id = tr.tournament_id
                AND tr.status = 'CONFIRMED'
            LEFT JOIN matches m
                ON t.tournament_id = m.tournament_id
            GROUP BY
                t.tournament_id,
                t.game_id,
                t.tournament_name,
                t.start_date,
                t.end_date,
                t.prize_pool,
                t.status
            ORDER BY t.tournament_id
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new TournamentOverviewResponse(
                        rs.getLong("tournament_id"),
                        rs.getLong("game_id"),
                        rs.getString("tournament_name"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getBigDecimal("prize_pool"),
                        rs.getString("status"),
                        rs.getLong("registered_teams"),
                        rs.getLong("total_matches"),
                        rs.getLong("completed_matches")
                )
        );
    }
}