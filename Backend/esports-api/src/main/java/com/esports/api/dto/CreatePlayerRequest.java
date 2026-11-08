package com.esports.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record CreatePlayerRequest(

        @NotBlank
        @Size(max = 100)
        String playerName,

        @Size(max = 100)
        String country,

        @Past
        LocalDate dateOfBirth

) {}