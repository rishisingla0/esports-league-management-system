package com.esports.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MatchResultRequest(

        @NotNull(message = "Placement is required")
        @Min(value = 1, message = "Placement must be at least 1")
        Integer placement,

        @NotNull(message = "Total points are required")
        @DecimalMin(value = "0.0", message = "Points cannot be negative")
        BigDecimal totalPoints

) {}