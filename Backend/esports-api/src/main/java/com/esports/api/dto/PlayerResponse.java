package com.esports.api.dto;

import java.time.LocalDate;

public record PlayerResponse(
        Long playerId,
        String playerName,
        String country,
        LocalDate dateOfBirth
) {}