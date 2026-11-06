package com.esports.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateTeamSponsorshipRequest(
    @NotNull Long teamId,
    @NotNull Long sponsorId,
    @NotNull LocalDate startDate,
    LocalDate endDate,
    @NotNull @DecimalMin("0.00") BigDecimal sponsorshipAmount,
    @NotBlank @Pattern(regexp = "ACTIVE|COMPLETED|CANCELLED") String status
) {}