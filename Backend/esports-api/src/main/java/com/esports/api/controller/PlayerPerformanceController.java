package com.esports.api.controller;

import com.esports.api.dto.CreatePlayerPerformanceRequest;
import com.esports.api.dto.UpdatePlayerPerformanceRequest;
import com.esports.api.dto.PlayerPerformanceResponse;
import com.esports.api.service.PlayerPerformanceService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player-performances")
public class PlayerPerformanceController {

    private final PlayerPerformanceService performanceService;

    public PlayerPerformanceController(
            PlayerPerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    // Get all performances
    @GetMapping
    public List<PlayerPerformanceResponse> getAllPerformances() {
        return performanceService.getAllPerformances();
    }

    // Get performance by ID
    @GetMapping("/{id}")
    public PlayerPerformanceResponse getPerformanceById(
            @PathVariable Long id) {
        return performanceService.getPerformanceById(id);
    }

    // Get performances by match
    @GetMapping("/match/{matchId}")
    public List<PlayerPerformanceResponse> getPerformancesByMatch(
            @PathVariable Long matchId) {
        return performanceService.getPerformancesByMatch(matchId);
    }

    // Get performances by player
    @GetMapping("/player/{playerId}")
    public List<PlayerPerformanceResponse> getPerformancesByPlayer(
            @PathVariable Long playerId) {
        return performanceService.getPerformancesByPlayer(playerId);
    }

    // Create performance
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerPerformanceResponse addPerformance(
            @Valid @RequestBody CreatePlayerPerformanceRequest request) {
        return performanceService.addPerformance(request);
    }

    // Update performance
    @PutMapping("/{id}")
    public PlayerPerformanceResponse updatePerformance(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePlayerPerformanceRequest request) {
        return performanceService.updatePerformance(id, request);
    }

    // Delete performance 
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerformance(@PathVariable Long id) {
        performanceService.deletePerformance(id);
    }
}