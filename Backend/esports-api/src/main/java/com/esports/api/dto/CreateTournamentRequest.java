package com.esports.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTournamentRequest(

    @NotNull
    Long gameId,

    @NotBlank
    @Size(max = 150)
    String tournamentName,

    @NotNull
    LocalDate startDate,

    @NotNull
    LocalDate endDate,

    @DecimalMin("0.00")
    @Digits(integer = 12, fraction = 2)
    BigDecimal prizePool,

    @Pattern(regexp = "(?i)(UPCOMING|ONGOING|COMPLETED|CANCELLED)")
    String status

) {}