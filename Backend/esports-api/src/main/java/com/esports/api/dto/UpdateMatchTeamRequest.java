
package com.esports.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

public record UpdateMatchTeamRequest(

        @NotNull(message = "Placement is required")
        @Positive(message = "Placement must be positive")
        Integer placement,

        @NotNull(message = "Total points are required")
        @DecimalMin(value = "0.00", message = "Total points cannot be negative")
        @Digits(integer = 6, fraction = 2,
                message = "Total points must have at most 6 integer digits and 2 decimal places")
        BigDecimal totalPoints

) {
}
