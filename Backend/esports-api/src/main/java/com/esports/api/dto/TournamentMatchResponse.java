package com.esports.api.dto;

import java.time.LocalDateTime;

public record TournamentMatchResponse(
        Long matchId,
        Integer matchNumber,
        String mapName,
        LocalDateTime scheduledAt,
        String status,
        Long participatingTeams
) {}