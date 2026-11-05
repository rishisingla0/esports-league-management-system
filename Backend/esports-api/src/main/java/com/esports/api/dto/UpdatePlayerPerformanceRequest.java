package com.esports.api.dto;

import jakarta.validation.constraints.PositiveOrZero;

public record UpdatePlayerPerformanceRequest(

        @PositiveOrZero
        Integer kills,

        @PositiveOrZero
        Integer damage,

        @PositiveOrZero
        Integer survivalTimeSeconds,

        @PositiveOrZero
        Integer assists

) {}