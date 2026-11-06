
package com.esports.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpdateCoachRequest(

    @NotBlank
    @Size(max = 100)
    String coachName,

    @Size(max = 100)
    String country,

    LocalDate hireDate

) {}
