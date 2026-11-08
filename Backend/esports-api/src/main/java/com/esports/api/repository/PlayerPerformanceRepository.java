package com.esports.api.repository;

import com.esports.api.dto.PlayerPerformanceRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
public class PlayerPerformanceRepository {

    private final JdbcTemplate jdbcTemplate;

    public PlayerPerformanceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String SELECT_SQL = """
            SELECT
                pp.performance_id AS performanceId,
                pp.match_squad_player_id AS matchSquadPlayerId,
                msp.match_id AS matchId,
                msp.player_id AS playerId,
                p.player_name AS playerName,
                mt.team_id AS teamId,
                t.team_name AS teamName,
                pp.kills,
                pp.damage,
                pp.survival_time_seconds AS survivalTimeSeconds,
                pp.assists
            FROM player_performances pp
            JOIN match_squad_players msp
                ON pp.match_squad_player_id = msp.match_squad_player_id
            JOIN players p
                ON msp.player_id = p.player_id
            JOIN match_teams mt
                ON msp.match_team_id = mt.match_team_id
            JOIN teams t
                ON mt.team_id = t.team_id
            """;

    public List<Map<String, Object>> getAll() {
        return jdbcTemplate.queryForList(
                SELECT_SQL + " ORDER BY pp.performance_id"
        );
    }

    public List<Map<String, Object>> getById(Long id) {
        return jdbcTemplate.queryForList(
                SELECT_SQL + " WHERE pp.performance_id = ?", id
        );
    }

    public List<Map<String, Object>> getByMatch(Long matchId) {
        return jdbcTemplate.queryForList(
                SELECT_SQL + " WHERE msp.match_id = ? ORDER BY p.player_name",
                matchId
        );
    }

    public Long create(PlayerPerformanceRequest request) {

        String sql = """
                INSERT INTO player_performances
                (match_squad_player_id, kills, damage,
                 survival_time_seconds, assists)
                VALUES (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, request.matchSquadPlayerId());
            ps.setInt(2, request.kills());
            ps.setInt(3, request.damage());

            if (request.survivalTimeSeconds() == null) {
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(4, request.survivalTimeSeconds());
            }

            ps.setInt(5, request.assists());

            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int update(Long id, PlayerPerformanceRequest request) {

        String sql = """
                UPDATE player_performances
                SET match_squad_player_id = ?,
                    kills = ?,
                    damage = ?,
                    survival_time_seconds = ?,
                    assists = ?
                WHERE performance_id = ?
                """;

        return jdbcTemplate.update(
                sql,
                request.matchSquadPlayerId(),
                request.kills(),
                request.damage(),
                request.survivalTimeSeconds(),
                request.assists(),
                id
        );
    }

    public int delete(Long id) {
        return jdbcTemplate.update(
                "DELETE FROM player_performances WHERE performance_id = ?",
                id
        );
    }
    
public boolean areAllPlayersRecorded(Long matchId) {

    String sql = """
            SELECT COUNT(*)
            FROM match_squad_players msp
            LEFT JOIN player_performances pp
                ON msp.match_squad_player_id = pp.match_squad_player_id
            WHERE msp.match_id = ?
              AND pp.performance_id IS NULL
            """;

    Integer missingCount = jdbcTemplate.queryForObject(
            sql, Integer.class, matchId
    );

    return missingCount != null && missingCount == 0;
}

}