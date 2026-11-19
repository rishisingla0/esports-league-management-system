package com.esports.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(

    @NotBlank
    @Email
    String email,

    @NotBlank
    String password,

    @NotBlank
    @Pattern(
        regexp = "(?i)(TEAM_MANAGER|ORGANIZER|PLAYER|VIEWER)",
        message = "Invalid account type"
    )
    String role

) {}