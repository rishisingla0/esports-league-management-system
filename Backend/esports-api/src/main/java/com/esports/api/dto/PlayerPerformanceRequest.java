package com.esports.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PlayerPerformanceRequest(

        @NotNull(message = "Match squad player ID is required")
        @Positive(message = "Match squad player ID must be positive")
        Long matchSquadPlayerId,

        @NotNull(message = "Kills are required")
        @Min(value = 0, message = "Kills cannot be negative")
        Integer kills,

        @NotNull(message = "Damage is required")
        @Min(value = 0, message = "Damage cannot be negative")
        Integer damage,

        @Min(value = 0, message = "Survival time cannot be negative")
        Integer survivalTimeSeconds,

        @NotNull(message = "Assists are required")
        @Min(value = 0, message = "Assists cannot be negative")
        Integer assists

) {}