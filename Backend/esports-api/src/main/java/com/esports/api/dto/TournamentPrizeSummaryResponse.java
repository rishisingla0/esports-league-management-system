package com.esports.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record TournamentPrizeSummaryResponse(
        Long tournamentId,
        String tournamentName,
        BigDecimal totalPrizeDistributed,
        Long teamsAwarded,
        List<PrizeAward> awards
) {
    public record PrizeAward(
            Long teamId,
            String teamName,
            Integer placement,
            BigDecimal prizeAmount,
            LocalDate awardedAt
    ) {}
}