
package com.esports.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TeamOnboardingRequest(

        @NotNull(message = "Organization ID is required")
        Long organizationId,

        @NotNull(message = "Game ID is required")
        Long gameId,

        @NotBlank(message = "Team name is required")
        @Size(max = 100, message = "Team name cannot exceed 100 characters")
        String teamName

) {}
