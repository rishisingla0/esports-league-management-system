package com.esports.api.dto;

import java.math.BigDecimal;

public record DashboardSummaryResponse(
        long totalGames,
        long totalTeams,
        long totalPlayers,
        long totalOrganizations,
        long totalTournaments,
        long ongoingMatches,
        long scheduledMatches,
        BigDecimal totalPrizeMoneyAwarded,
        BigDecimal activeSponsorshipValue
) {
}