package com.esports.api.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record CreateMatchRequest(

    @NotNull
    Long tournamentId,

    @NotNull
    @Positive
    Integer matchNumber,

    @NotBlank
    @Size(max = 100)
    String mapName,

    LocalDateTime scheduledAt

) {}