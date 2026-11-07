package com.esports.api.repository;

import com.esports.api.dto.MatchTeamComparisonResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatchTeamComparisonRepository {

    private final JdbcTemplate jdbcTemplate;

    public MatchTeamComparisonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MatchTeamComparisonResponse> getTeamComparison(Long matchId) {

        String sql = """
                SELECT
                    t.team_id,
                    t.team_name,
                    mt.placement,
                    COALESCE(mt.total_points, 0) AS total_points,
                    COALESCE(SUM(pp.kills), 0) AS total_kills,
                    COALESCE(SUM(pp.damage), 0) AS total_damage,
                    COALESCE(SUM(pp.assists), 0) AS total_assists
                FROM match_teams mt
                JOIN teams t
                    ON mt.team_id = t.team_id
                LEFT JOIN match_squad_players msp
                    ON mt.match_team_id = msp.match_team_id
                LEFT JOIN player_performances pp
                    ON msp.match_squad_player_id = pp.match_squad_player_id
                WHERE mt.match_id = ?
                GROUP BY
                    t.team_id,
                    t.team_name,
                    mt.placement,
                    mt.total_points
                ORDER BY
                    mt.total_points DESC,
                    mt.placement ASC,
                    t.team_name ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new MatchTeamComparisonResponse(
                        rs.getLong("team_id"),
                        rs.getString("team_name"),
                        (Integer) rs.getObject("placement"),
                        rs.getBigDecimal("total_points"),
                        rs.getLong("total_kills"),
                        rs.getLong("total_damage"),
                        rs.getLong("total_assists")
                ),
                matchId
        );
    }
}