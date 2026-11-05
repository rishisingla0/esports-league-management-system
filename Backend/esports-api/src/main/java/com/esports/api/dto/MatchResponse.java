package com.esports.api.dto;

import java.time.LocalDateTime;

public record MatchResponse(

    Long matchId,
    Long tournamentId,
    String tournamentName,
    Integer matchNumber,
    String mapName,
    LocalDateTime scheduledAt,
    String status

) {}