
package com.esports.api.controller;

import com.esports.api.dto.CreateOrganizationAndTeamRequest;
import com.esports.api.dto.TeamOnboardingRequest;
import com.esports.api.dto.TeamOnboardingResponse;
import com.esports.api.dto.ManagerProfileResponse;
import com.esports.api.service.TeamManagerOnboardingService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
public class TeamManagerOnboardingController {

    private final TeamManagerOnboardingService onboardingService;

    public TeamManagerOnboardingController(
            TeamManagerOnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @PostMapping("/create-organization")
    @ResponseStatus(HttpStatus.CREATED)
    public TeamOnboardingResponse createOrganizationAndTeam(
            Authentication authentication,
            @Valid @RequestBody CreateOrganizationAndTeamRequest request) {

        return onboardingService.createOrganizationAndTeam(
                authentication.getName(), request);
    }

    @PostMapping("/join-organization")
    @ResponseStatus(HttpStatus.CREATED)
    public TeamOnboardingResponse createTeamInExistingOrganization(
            Authentication authentication,
            @Valid @RequestBody TeamOnboardingRequest request) {

        return onboardingService.createTeamInExistingOrganization(
                authentication.getName(), request);
    }

    @GetMapping("/me")
    public ManagerProfileResponse getMyProfile(
            Authentication authentication) {

        return onboardingService.getMyProfile(authentication.getName());
    }
}
