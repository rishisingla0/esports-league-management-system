package com.esports.api.controller;

import com.esports.api.dto.CreateMatchTeamRequest;
import com.esports.api.dto.MatchTeamResponse;
import com.esports.api.dto.UpdateMatchTeamRequest;
import com.esports.api.service.MatchTeamService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match-teams")
public class MatchTeamController {

    private final MatchTeamService matchTeamService;

    public MatchTeamController(MatchTeamService matchTeamService) {
        this.matchTeamService = matchTeamService;
    }

    // GET ALL MATCH TEAMS
    @GetMapping
    public List<MatchTeamResponse> getAllMatchTeams() {
        return matchTeamService.getAllMatchTeams();
    }

    // GET MATCH TEAM BY ID
    @GetMapping("/{id}")
    public MatchTeamResponse getMatchTeamById(@PathVariable Long id) {
        return matchTeamService.getMatchTeamById(id);
    }

    // GET TEAMS BY MATCH ID
    @GetMapping("/match/{matchId}")
    public List<MatchTeamResponse> getTeamsByMatchId(
            @PathVariable Long matchId
    ) {
        return matchTeamService.getTeamsByMatchId(matchId);
    }

    // CREATE MATCH TEAM
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchTeamResponse createMatchTeam(
            @Valid @RequestBody CreateMatchTeamRequest request
    ) {
        return matchTeamService.createMatchTeam(request);
    }

    // UPDATE MATCH TEAM
    @PutMapping("/{id}")
    public MatchTeamResponse updateMatchTeam(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMatchTeamRequest request
    ) {
        return matchTeamService.updateMatchTeam(id, request);
    }

    // DELETE MATCH TEAM
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMatchTeam(@PathVariable Long id) {
        matchTeamService.deleteMatchTeam(id);
    }
}