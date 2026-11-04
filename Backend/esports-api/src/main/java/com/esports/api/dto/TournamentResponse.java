package com.esports.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TournamentResponse(
    Long tournamentId,
    Long gameId,
    String gameName,
    String tournamentName,
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal prizePool,
    String status
) {}