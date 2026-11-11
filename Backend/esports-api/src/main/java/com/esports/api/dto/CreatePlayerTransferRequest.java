package com.esports.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePlayerTransferRequest(
    @NotNull @Positive Long playerId,
    @NotNull @Positive Long fromTeamId,
    @NotNull @Positive Long toTeamId,
    @NotNull LocalDate transferDate,
    @NotNull @DecimalMin("0.00") BigDecimal transferFee,
    @Positive Integer contractMonths
) {}