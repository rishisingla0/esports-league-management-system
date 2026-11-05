package com.esports.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateMatchTeamRequest(

        @NotNull(message = "Match ID is required")
        @Positive(message = "Match ID must be positive")
        Long matchId,

        @NotNull(message = "Team ID is required")
        @Positive(message = "Team ID must be positive")
        Long teamId

) {
}