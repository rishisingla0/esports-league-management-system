package com.esports.api.repository;

import com.esports.api.dto.LeaderboardResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LeaderboardRepository {

    private final JdbcTemplate jdbcTemplate;

    public LeaderboardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<LeaderboardResponse> getTopKills() {
        String sql = """
                SELECT p.player_id, p.player_name, t.team_name,
                       SUM(pp.kills) AS total_kills,
                       SUM(pp.damage) AS total_damage,
                       SUM(pp.assists) AS total_assists
                FROM player_performances pp
                JOIN match_squad_players msp
                    ON pp.match_squad_player_id = msp.match_squad_player_id
                JOIN players p ON msp.player_id = p.player_id
                JOIN match_teams mt ON msp.match_team_id = mt.match_team_id
                JOIN teams t ON mt.team_id = t.team_id
                GROUP BY p.player_id, p.player_name, t.team_name
                ORDER BY total_kills DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new LeaderboardResponse(
                        rs.getLong("player_id"),
                        rs.getString("player_name"),
                        rs.getString("team_name"),
                        rs.getLong("total_kills"),
                        rs.getLong("total_damage"),
                        rs.getLong("total_assists")
                ));
    }

    public List<LeaderboardResponse> getTopDamage() {
        String sql = """
                SELECT p.player_id, p.player_name, t.team_name,
                       SUM(pp.kills) AS total_kills,
                       SUM(pp.damage) AS total_damage,
                       SUM(pp.assists) AS total_assists
                FROM player_performances pp
                JOIN match_squad_players msp
                    ON pp.match_squad_player_id = msp.match_squad_player_id
                JOIN players p ON msp.player_id = p.player_id
                JOIN match_teams mt ON msp.match_team_id = mt.match_team_id
                JOIN teams t ON mt.team_id = t.team_id
                GROUP BY p.player_id, p.player_name, t.team_name
                ORDER BY total_damage DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new LeaderboardResponse(
                        rs.getLong("player_id"),
                        rs.getString("player_name"),
                        rs.getString("team_name"),
                        rs.getLong("total_kills"),
                        rs.getLong("total_damage"),
                        rs.getLong("total_assists")
                ));
    }

    public List<LeaderboardResponse> getTopAssists() {
        String sql = """
                SELECT p.player_id, p.player_name, t.team_name,
                       SUM(pp.kills) AS total_kills,
                       SUM(pp.damage) AS total_damage,
                       SUM(pp.assists) AS total_assists
                FROM player_performances pp
                JOIN match_squad_players msp
                    ON pp.match_squad_player_id = msp.match_squad_player_id
                JOIN players p ON msp.player_id = p.player_id
                JOIN match_teams mt ON msp.match_team_id = mt.match_team_id
                JOIN teams t ON mt.team_id = t.team_id
                GROUP BY p.player_id, p.player_name, t.team_name
                ORDER BY total_assists DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new LeaderboardResponse(
                        rs.getLong("player_id"),
                        rs.getString("player_name"),
                        rs.getString("team_name"),
                        rs.getLong("total_kills"),
                        rs.getLong("total_damage"),
                        rs.getLong("total_assists")
                ));
    }
}