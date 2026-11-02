package com.esports.api.dto;

import java.time.LocalDateTime;

public record TeamResponse(
    Long teamId,
    String teamName,

    Long organizationId,
    String organizationName,

    Long gameId,
    String gameName,

    Long coachId,
    String coachName,

    LocalDateTime createdAt
) {}