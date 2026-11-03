
package com.esports.api.controller;
import com.esports.api.dto.TeamResponse;
import com.esports.api.dto.CreateTeamRequest;
import com.esports.api.service.TeamService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.esports.api.dto.UpdateTeamRequest;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }
@PutMapping("/{id}")
public TeamResponse updateTeam(
        @PathVariable Long id,
        @Valid @RequestBody UpdateTeamRequest request) {

    return teamService.updateTeam(id, request);
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
public TeamResponse createTeam(@Valid @RequestBody CreateTeamRequest request) {
    return teamService.createTeam(request);
}
@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void deleteTeam(@PathVariable Long id) {
    teamService.deleteTeam(id);
}
}
