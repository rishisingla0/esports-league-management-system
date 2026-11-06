package com.esports.api.controller;

import com.esports.api.dto.*;
import com.esports.api.service.TeamRosterService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team-rosters")
public class TeamRosterController {

    private final TeamRosterService service;

    public TeamRosterController(TeamRosterService service) {
        this.service = service;
    }

    @GetMapping
    public List<TeamRosterResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public TeamRosterResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/team/{teamId}")
    public List<TeamRosterResponse> getByTeam(@PathVariable Long teamId) {
        return service.getByTeam(teamId);
    }

    @GetMapping("/player/{playerId}")
    public List<TeamRosterResponse> getByPlayer(@PathVariable Long playerId) {
        return service.getByPlayer(playerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamRosterResponse create(
            @Valid @RequestBody CreateTeamRosterRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public TeamRosterResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTeamRosterRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}