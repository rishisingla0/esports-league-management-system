package com.esports.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateSponsorRequest(
    @NotBlank @Size(max = 150) String sponsorName,
    @Size(max = 100) String industry,
    @Size(max = 100) String country,
    @Size(max = 255) String website
) {}