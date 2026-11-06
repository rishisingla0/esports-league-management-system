package com.esports.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PrizeDistributionResponse(
    Long prizeDistributionId,
    Long tournamentId,
    Long teamId,
    Integer placement,
    BigDecimal prizeAmount,
    LocalDate awardedAt
) {}