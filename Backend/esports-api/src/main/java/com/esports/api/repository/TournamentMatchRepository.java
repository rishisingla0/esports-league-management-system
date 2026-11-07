package com.esports.api.repository;

import com.esports.api.dto.TournamentMatchResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TournamentMatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public TournamentMatchRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TournamentMatchResponse> getTournamentMatches(Long tournamentId) {

        String sql = """
            SELECT
                m.match_id,
                m.match_number,
                m.map_name,
                m.scheduled_at,
                m.status,
                COUNT(mt.match_team_id) AS participating_teams
            FROM matches m
            LEFT JOIN match_teams mt
                ON m.match_id = mt.match_id
            WHERE m.tournament_id = ?
            GROUP BY
                m.match_id,
                m.match_number,
                m.map_name,
                m.scheduled_at,
                m.status
            ORDER BY m.match_number
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new TournamentMatchResponse(
                        rs.getLong("match_id"),
                        rs.getInt("match_number"),
                        rs.getString("map_name"),
                        rs.getTimestamp("scheduled_at") != null
                                ? rs.getTimestamp("scheduled_at").toLocalDateTime()
                                : null,
                        rs.getString("status"),
                        rs.getLong("participating_teams")
                ),
                tournamentId
        );
    }
}