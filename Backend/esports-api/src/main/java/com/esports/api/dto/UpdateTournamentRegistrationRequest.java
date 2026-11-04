package com.esports.api.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UpdateTournamentRegistrationRequest(

    @NotNull
    Long tournamentId,

    @NotNull
    Long teamId,

    @NotNull
    LocalDate registrationDate,

    @Positive
    Integer seed,

    @Pattern(
        regexp = "(?i)(REGISTERED|CONFIRMED|WITHDRAWN)",
        message = "Status must be REGISTERED, CONFIRMED or WITHDRAWN"
    )
    String status

) {}