
package com.esports.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateMatchSquadPlayerRequest(

    @NotNull(message = "Match Team ID is required")
    @Positive(message = "Match Team ID must be positive")
    Long matchTeamId,

    @NotNull(message = "Player ID is required")
    @Positive(message = "Player ID must be positive")
    Long playerId

) {}
