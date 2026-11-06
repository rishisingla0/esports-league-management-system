package com.esports.api.dto;

import java.time.LocalDate;

public record TeamRosterResponse(
    Long rosterId,
    Long teamId,
    Long playerId,
    String role,
    LocalDate startDate,
    LocalDate endDate,
    String status
) {}