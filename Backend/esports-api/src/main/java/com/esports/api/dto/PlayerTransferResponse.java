package com.esports.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PlayerTransferResponse(
    Long transferId,
    Long playerId,
    Long fromTeamId,
    Long toTeamId,
    LocalDate transferDate,
    BigDecimal transferFee,
    Integer contractMonths
) {}