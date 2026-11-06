package com.esports.api.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UpdateTeamRosterRequest(
    @NotNull Long teamId,
    @NotNull Long playerId,
    @Size(max = 50) String role,
    @NotNull LocalDate startDate,
    LocalDate endDate,
    @NotBlank
    @Pattern(regexp = "ACTIVE|INACTIVE")
    String status
) {}