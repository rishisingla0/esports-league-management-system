package com.esports.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AddManagerRosterPlayerRequest(
        @NotNull @Positive Long playerId,
        @Size(max = 50) String role,
        @NotNull LocalDate startDate
) {}
