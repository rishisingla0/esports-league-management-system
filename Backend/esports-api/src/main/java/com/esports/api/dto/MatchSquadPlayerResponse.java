
package com.esports.api.dto;

public record MatchSquadPlayerResponse(
    Long matchSquadPlayerId,
    Long matchId,
    Long matchTeamId,
    Long teamId,
    String teamName,
    Long playerId,
    String playerName
) {}
