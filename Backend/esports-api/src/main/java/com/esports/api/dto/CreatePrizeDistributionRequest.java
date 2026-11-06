package com.esports.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePrizeDistributionRequest(
    @NotNull Long tournamentId,
    @NotNull Long teamId,
    @NotNull @Positive Integer placement,
    @NotNull @DecimalMin("0.00") BigDecimal prizeAmount,
    LocalDate awardedAt
) {}