package com.esports.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TeamSponsorshipResponse(
    Long sponsorshipId,
    Long teamId,
    Long sponsorId,
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal sponsorshipAmount,
    String status
) {}