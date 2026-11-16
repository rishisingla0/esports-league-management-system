
package com.esports.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateOrganizationAndTeamRequest(

        @NotBlank(message = "Organization name is required")
        @Size(max = 150, message = "Organization name cannot exceed 150 characters")
        String organizationName,

        @Size(max = 100)
        String country,

        LocalDate foundedDate,

        @Size(max = 255)
        String website,

        @NotNull(message = "Game ID is required")
        Long gameId,

        @NotBlank(message = "Team name is required")
        @Size(max = 100, message = "Team name cannot exceed 100 characters")
        String teamName

) {}
