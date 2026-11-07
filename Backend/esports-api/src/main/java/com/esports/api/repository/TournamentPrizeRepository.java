package com.esports.api.repository;

import com.esports.api.dto.TournamentPrizeSummaryResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TournamentPrizeRepository {

    private final JdbcTemplate jdbcTemplate;

    public TournamentPrizeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TournamentPrizeSummaryResponse getPrizeSummary(Long tournamentId) {

        String tournamentSql = """
            SELECT tournament_id, tournament_name
            FROM tournaments
            WHERE tournament_id = ?
            """;

        var tournament = jdbcTemplate.query(
                tournamentSql,
                (rs, rowNum) -> new Object[]{
                        rs.getLong("tournament_id"),
                        rs.getString("tournament_name")
                },
                tournamentId
        );

        if (tournament.isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Tournament not found"
            );
        }

        String awardsSql = """
            SELECT
                pd.team_id,
                tm.team_name,
                pd.placement,
                pd.prize_amount,
                pd.awarded_at
            FROM prize_distributions pd
            JOIN teams tm ON pd.team_id = tm.team_id
            WHERE pd.tournament_id = ?
            ORDER BY pd.placement
            """;

        List<TournamentPrizeSummaryResponse.PrizeAward> awards =
                jdbcTemplate.query(awardsSql, (rs, rowNum) ->
                        new TournamentPrizeSummaryResponse.PrizeAward(
                                rs.getLong("team_id"),
                                rs.getString("team_name"),
                                rs.getInt("placement"),
                                rs.getBigDecimal("prize_amount"),
                                rs.getDate("awarded_at") != null
                                        ? rs.getDate("awarded_at").toLocalDate()
                                        : null
                        ), tournamentId);

        String summarySql = """
            SELECT
                COALESCE(SUM(prize_amount), 0) AS total_distributed,
                COUNT(DISTINCT team_id) AS teams_awarded
            FROM prize_distributions
            WHERE tournament_id = ?
            """;

        var summary = jdbcTemplate.queryForMap(summarySql, tournamentId);

        return new TournamentPrizeSummaryResponse(
                (Long) tournament.get(0)[0],
                (String) tournament.get(0)[1],
                (java.math.BigDecimal) summary.get("total_distributed"),
                ((Number) summary.get("teams_awarded")).longValue(),
                awards
        );
    }
}