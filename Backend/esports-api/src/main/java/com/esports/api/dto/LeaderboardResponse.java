package com.esports.api.dto;

public record LeaderboardResponse(
        Long playerId,
        String playerName,
        String teamName,
        Long totalKills,
        Long totalDamage,
        Long totalAssists
) {
}