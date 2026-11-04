package com.esports.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateOrganizationRequest(

        @NotBlank
        @Size(max = 150)
        String organizationName,

        @Size(max = 100)
        String country,

        LocalDate foundedDate,

        @Size(max = 255)
        String website

) {}