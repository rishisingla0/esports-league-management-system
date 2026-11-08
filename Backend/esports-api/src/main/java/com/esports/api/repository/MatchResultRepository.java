package com.esports.api.repository;

import com.esports.api.dto.MatchResultRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MatchResultRepository {

    private final JdbcTemplate jdbcTemplate;

    public MatchResultRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getResults(Long matchId) {

        String sql = """
                SELECT
                    mt.match_team_id AS matchTeamId,
                    mt.match_id AS matchId,
                    mt.team_id AS teamId,
                    t.team_name AS teamName,
                    mt.placement,
                    mt.total_points AS totalPoints
                FROM match_teams mt
                JOIN teams t ON t.team_id = mt.team_id
                WHERE mt.match_id = ?
                ORDER BY mt.placement IS NULL, mt.placement ASC
                """;

        return jdbcTemplate.queryForList(sql, matchId);
    }

    public int updateResult(Long matchTeamId, MatchResultRequest request) {

        String sql = """
                UPDATE match_teams
                SET placement = ?,
                    total_points = ?
                WHERE match_team_id = ?
                """;

        return jdbcTemplate.update(
                sql,
                request.placement(),
                request.totalPoints(),
                matchTeamId
        );
    }
}