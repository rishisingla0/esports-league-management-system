package com.esports.api.dto;

import java.time.LocalDateTime;

public record MatchOverviewResponse(
        Long matchId,
        Integer matchNumber,
        Long tournamentId,
        String tournamentName,
        String mapName,
        LocalDateTime scheduledAt,
        String status,
        Long participatingTeams,
        Long participatingPlayers
) {}