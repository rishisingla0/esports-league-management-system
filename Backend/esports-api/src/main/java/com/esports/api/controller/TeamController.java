
package com.esports.api.controller;

import com.esports.api.dto.TeamResponse;
import com.esports.api.dto.CreateTeamRequest;
import com.esports.api.dto.UpdateTeamRequest;
import com.esports.api.service.TeamService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<TeamResponse> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public TeamResponse getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamResponse createTeam(
            @Valid @RequestBody CreateTeamRequest request,
            Authentication authentication) {

        return teamService.createTeam(authentication.getName(), request);
    }

    @PutMapping("/{id}")
    public TeamResponse updateTeam(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTeamRequest request,
            Authentication authentication) {

        return teamService.updateTeam(authentication.getName(), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(
            @PathVariable Long id,
            Authentication authentication) {

        teamService.deleteTeam(authentication.getName(), id);
    }
}
