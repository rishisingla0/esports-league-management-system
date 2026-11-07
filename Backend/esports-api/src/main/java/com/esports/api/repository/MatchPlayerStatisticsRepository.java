package com.esports.api.repository;

import com.esports.api.dto.MatchPlayerStatisticsResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatchPlayerStatisticsRepository {

    private final JdbcTemplate jdbcTemplate;

    public MatchPlayerStatisticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MatchPlayerStatisticsResponse> getPlayerStatistics(Long matchId) {

        String sql = """
                SELECT
                    p.player_id,
                    p.player_name,
                    t.team_id,
                    t.team_name,
                    COALESCE(pp.kills, 0) AS kills,
                    COALESCE(pp.damage, 0) AS damage,
                    COALESCE(pp.assists, 0) AS assists,
                    pp.survival_time_seconds
                FROM match_teams mt
                JOIN teams t
                    ON mt.team_id = t.team_id
                JOIN match_squad_players msp
                    ON mt.match_team_id = msp.match_team_id
                JOIN players p
                    ON msp.player_id = p.player_id
                LEFT JOIN player_performances pp
                    ON msp.match_squad_player_id = pp.match_squad_player_id
                WHERE mt.match_id = ?
                ORDER BY t.team_name, p.player_name
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new MatchPlayerStatisticsResponse(
                        rs.getLong("player_id"),
                        rs.getString("player_name"),
                        rs.getLong("team_id"),
                        rs.getString("team_name"),
                        rs.getInt("kills"),
                        rs.getInt("damage"),
                        rs.getInt("assists"),
                        (Integer) rs.getObject("survival_time_seconds")
                ),
                matchId
        );
    }
}