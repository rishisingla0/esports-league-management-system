package com.esports.api.dto;

public record SponsorResponse(
    Long sponsorId,
    String sponsorName,
    String industry,
    String country,
    String website
) {}