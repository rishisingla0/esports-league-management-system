package com.esports.api.controller;

import com.esports.api.dto.*;
import com.esports.api.service.PrizeDistributionService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prize-distributions")
public class PrizeDistributionController {

    private final PrizeDistributionService service;

    public PrizeDistributionController(PrizeDistributionService service) {
        this.service = service;
    }

    @GetMapping
    public List<PrizeDistributionResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public PrizeDistributionResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/tournament/{tournamentId}")
    public List<PrizeDistributionResponse> getByTournament(
            @PathVariable Long tournamentId) {
        return service.getByTournament(tournamentId);
    }

    @GetMapping("/team/{teamId}")
    public List<PrizeDistributionResponse> getByTeam(@PathVariable Long teamId) {
        return service.getByTeam(teamId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrizeDistributionResponse create(
            @Valid @RequestBody CreatePrizeDistributionRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public PrizeDistributionResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePrizeDistributionRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}