package com.esports.api.controller;

import com.esports.api.dto.*;
import com.esports.api.service.TeamSponsorshipService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team-sponsorships")
public class TeamSponsorshipController {

    private final TeamSponsorshipService service;

    public TeamSponsorshipController(TeamSponsorshipService service) {
        this.service = service;
    }

    @GetMapping
    public List<TeamSponsorshipResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public TeamSponsorshipResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/team/{teamId}")
    public List<TeamSponsorshipResponse> getByTeam(@PathVariable Long teamId) {
        return service.getByTeam(teamId);
    }

    @GetMapping("/sponsor/{sponsorId}")
    public List<TeamSponsorshipResponse> getBySponsor(@PathVariable Long sponsorId) {
        return service.getBySponsor(sponsorId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamSponsorshipResponse create(
            @Valid @RequestBody CreateTeamSponsorshipRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public TeamSponsorshipResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTeamSponsorshipRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}