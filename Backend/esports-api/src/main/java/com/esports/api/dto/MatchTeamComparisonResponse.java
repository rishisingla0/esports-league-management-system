package com.esports.api.dto;

import java.math.BigDecimal;

public record MatchTeamComparisonResponse(
        Long teamId,
        String teamName,
        Integer placement,
        BigDecimal totalPoints,
        Long totalKills,
        Long totalDamage,
        Long totalAssists
) {}