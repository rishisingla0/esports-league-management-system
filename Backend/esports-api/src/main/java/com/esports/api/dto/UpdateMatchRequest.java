package com.esports.api.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record UpdateMatchRequest(

@NotNull
@Positive
Long tournamentId,

    @NotNull
    @Positive
    Integer matchNumber,

    @NotBlank
    @Size(max = 100)
    String mapName,

    LocalDateTime scheduledAt,

    @NotBlank
    @Pattern(
        regexp = "(?i)(SCHEDULED|ONGOING|COMPLETED|CANCELLED)",
        message = "Invalid match status"
    )
    String status

) {}