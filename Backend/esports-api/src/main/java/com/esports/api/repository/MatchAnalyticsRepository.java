package com.esports.api.repository;

import com.esports.api.dto.MatchOverviewResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatchAnalyticsRepository {

    private final JdbcTemplate jdbcTemplate;

    public MatchAnalyticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MatchOverviewResponse> getMatchOverview() {

        String sql = """
                SELECT
                    m.match_id,
                    m.match_number,
                    t.tournament_id,
                    t.tournament_name,
                    m.map_name,
                    m.scheduled_at,
                    m.status,
                    COUNT(DISTINCT mt.team_id) AS participating_teams,
                    COUNT(DISTINCT msp.player_id) AS participating_players
                FROM matches m
                JOIN tournaments t
                    ON m.tournament_id = t.tournament_id
                LEFT JOIN match_teams mt
                    ON m.match_id = mt.match_id
                LEFT JOIN match_squad_players msp
                    ON mt.match_team_id = msp.match_team_id
                GROUP BY
                    m.match_id,
                    m.match_number,
                    t.tournament_id,
                    t.tournament_name,
                    m.map_name,
                    m.scheduled_at,
                    m.status
                ORDER BY m.match_id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new MatchOverviewResponse(
                        rs.getLong("match_id"),
                        rs.getInt("match_number"),
                        rs.getLong("tournament_id"),
                        rs.getString("tournament_name"),
                        rs.getString("map_name"),
                        rs.getTimestamp("scheduled_at") != null
                                ? rs.getTimestamp("scheduled_at").toLocalDateTime()
                                : null,
                        rs.getString("status"),
                        rs.getLong("participating_teams"),
                        rs.getLong("participating_players")
                )
        );
    }
}