package com.esports.api.controller;

import com.esports.api.dto.*;
import com.esports.api.service.MatchService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public List<MatchResponse> getAllMatches() {
        return matchService.getAllMatches();
    }

    @GetMapping("/{id}")
    public MatchResponse getMatchById(
            @PathVariable Long id) {

        return matchService.getMatchById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchResponse createMatch(
            @Valid @RequestBody CreateMatchRequest request) {

        return matchService.createMatch(request);
    }

    @PutMapping("/{id}")
    public MatchResponse updateMatch(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMatchRequest request) {

        return matchService.updateMatch(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
    }
}