package com.esports.api.dto;

public record MatchPlayerStatisticsResponse(
        Long playerId,
        String playerName,
        Long teamId,
        String teamName,
        Integer kills,
        Integer damage,
        Integer assists,
        Integer survivalTimeSeconds
) {}