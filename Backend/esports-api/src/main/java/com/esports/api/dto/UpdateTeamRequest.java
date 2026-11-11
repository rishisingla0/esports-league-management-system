package com.esports.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;

public record UpdateTeamRequest(

@Positive
@NotNull
Long organizationId,

@Positive
@NotNull
Long gameId,

        Long coachId,

        @NotBlank
        @Size(max = 100)
        String teamName

) {}