
package com.esports.api.dto;

public record TeamOnboardingResponse(
        Long userId,
        Long organizationId,
        String organizationName,
        Long teamId,
        String teamName,
        Long gameId,
        String gameName,
        String message
) {}
