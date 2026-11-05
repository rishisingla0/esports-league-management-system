package com.esports.api.dto;

import java.math.BigDecimal;

public record MatchTeamResponse(

        Long matchTeamId,
        Long matchId,
        Integer matchNumber,
        Long teamId,
        String teamName,
        Integer placement,
        BigDecimal totalPoints

) {
}