package com.esports.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TournamentOverviewResponse(
        Long tournamentId,
        Long gameId,
        String tournamentName,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal prizePool,
        String status,
        Long registeredTeams,
        Long totalMatches,
        Long completedMatches
) {}