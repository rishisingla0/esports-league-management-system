package com.esports.api.dto;

public record PlayerGameProfileResponse(
    Long profileId,
    Long playerId,
    String playerName,
    Long gameId,
    String gameName,
    String inGameId
) {}