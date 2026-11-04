package com.esports.api.dto;

import java.time.LocalDate;

public record TournamentRegistrationResponse(
    Long registrationId,
    Long tournamentId,
    String tournamentName,
    Long teamId,
    String teamName,
    LocalDate registrationDate,
    Integer seed,
    String status
) {}