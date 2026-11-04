package com.esports.api.dto;

import java.time.LocalDate;

public record OrganizationResponse(
        Long organizationId,
        String organizationName,
        String country,
        LocalDate foundedDate,
        String website
) {}