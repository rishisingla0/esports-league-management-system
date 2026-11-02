
package com.esports.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTeamRequest(

    @NotNull
    Long organizationId,

    @NotNull
    Long gameId,

    Long coachId,

    @NotBlank
    @Size(max = 100)
    String teamName

) {
}
