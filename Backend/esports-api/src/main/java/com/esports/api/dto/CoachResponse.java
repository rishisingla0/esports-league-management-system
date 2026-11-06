
package com.esports.api.dto;

import java.time.LocalDate;

public record CoachResponse(
    Long coachId,
    String coachName,
    String country,
    LocalDate hireDate
) {}
