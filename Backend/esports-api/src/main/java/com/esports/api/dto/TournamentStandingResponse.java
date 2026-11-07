package com.esports.api.dto;

import java.math.BigDecimal;

public record TournamentStandingResponse(
        Long teamId,
        String teamName,
        Long matchesPlayed,
        BigDecimal totalPoints,
        BigDecimal averagePlacement,
        Integer bestPlacement
) {}
