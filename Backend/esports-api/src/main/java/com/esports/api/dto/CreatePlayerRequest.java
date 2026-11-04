package com.esports.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreatePlayerRequest(

        @NotBlank
        @Size(max = 100)
        String playerName,

        @Size(max = 100)
        String country,

        LocalDate dateOfBirth

) {}