package com.esports.api.repository;

import com.esports.api.dto.MatchMvpResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatchMvpRepository {

    private final JdbcTemplate jdbcTemplate;

    public MatchMvpRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MatchMvpResponse> getMvp(Long matchId) {

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
                ORDER BY
                    COALESCE(pp.kills, 0) DESC,
                    COALESCE(pp.damage, 0) DESC,
                    COALESCE(pp.assists, 0) DESC,
                    p.player_id ASC
                LIMIT 1
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new MatchMvpResponse(
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