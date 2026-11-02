
package com.esports.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateGameRequest(
    @NotBlank
    @Size(max = 100)
    String gameName,

    @NotBlank
    @Size(max = 50)
    String genre,

    @NotBlank
    @Size(max = 100)
    String developer,

    LocalDate releaseDate
) {
}

