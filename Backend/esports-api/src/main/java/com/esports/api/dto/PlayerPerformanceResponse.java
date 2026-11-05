package com.esports.api.dto;

public record PlayerPerformanceResponse(
        Long performanceId,
        Long matchSquadPlayerId,
        Long matchId,
        Long matchTeamId,
        Long teamId,
        String teamName,
        Long playerId,
        String playerName,
        Integer kills,
        Integer damage,
        Integer survivalTimeSeconds,
        Integer assists
) {}