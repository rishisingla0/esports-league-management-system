package com.esports.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ManagerMatchResponse(
        Long matchId,
        Long tournamentId,
        String tournamentName,
        Integer matchNumber,
        String mapName,
        LocalDateTime scheduledAt,
        String matchStatus,
        Integer placement,
        BigDecimal totalPoints
) {}
