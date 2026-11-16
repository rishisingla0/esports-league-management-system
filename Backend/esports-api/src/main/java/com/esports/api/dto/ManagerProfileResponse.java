
package com.esports.api.dto;

public record ManagerProfileResponse(
        Long userId,
        String fullName,
        String email,
        String role,
        boolean onboarded,
        Long organizationId,
        String organizationName,
        Long teamId,
        String teamName,
        Long gameId,
        String gameName
) {}
