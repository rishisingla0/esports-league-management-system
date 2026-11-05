package com.esports.api.dto;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreatePlayerPerformanceRequest(

        @NotNull
        @Positive
        Long matchSquadPlayerId,

        @NotNull
        @PositiveOrZero
        Integer kills,

        @NotNull
        @PositiveOrZero
        Integer damage,

        @PositiveOrZero
        Integer survivalTimeSeconds,

        @NotNull
        @PositiveOrZero
        Integer assists

) {}