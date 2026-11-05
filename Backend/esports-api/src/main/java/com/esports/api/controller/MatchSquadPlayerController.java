
package com.esports.api.controller;

import com.esports.api.dto.CreateMatchSquadPlayerRequest;
import com.esports.api.dto.MatchSquadPlayerResponse;
import com.esports.api.service.MatchSquadPlayerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match-squad-players")
public class MatchSquadPlayerController {

    private final MatchSquadPlayerService squadPlayerService;

    public MatchSquadPlayerController(
            MatchSquadPlayerService squadPlayerService) {
        this.squadPlayerService = squadPlayerService;
    }

    @GetMapping
    public List<MatchSquadPlayerResponse> getAllSquadPlayers() {
        return squadPlayerService.getAllSquadPlayers();
    }

    @GetMapping("/{id}")
    public MatchSquadPlayerResponse getSquadPlayerById(
            @PathVariable Long id) {
        return squadPlayerService.getSquadPlayerById(id);
    }

    @GetMapping("/match-team/{matchTeamId}")
    public List<MatchSquadPlayerResponse> getPlayersByMatchTeam(
            @PathVariable Long matchTeamId) {
        return squadPlayerService.getPlayersByMatchTeamId(matchTeamId);
    }

    @GetMapping("/match/{matchId}")
    public List<MatchSquadPlayerResponse> getPlayersByMatch(
            @PathVariable Long matchId) {
        return squadPlayerService.getPlayersByMatchId(matchId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchSquadPlayerResponse addSquadPlayer(
            @Valid @RequestBody CreateMatchSquadPlayerRequest request) {
        return squadPlayerService.addSquadPlayer(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSquadPlayer(@PathVariable Long id) {
        squadPlayerService.removeSquadPlayer(id);
    }
}
