package com.esports.api.controller;

import com.esports.api.dto.*;
import com.esports.api.service.PlayerTransferService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player-transfers")
public class PlayerTransferController {

    private final PlayerTransferService service;

    public PlayerTransferController(PlayerTransferService service) {
        this.service = service;
    }

    @GetMapping
    public List<PlayerTransferResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public PlayerTransferResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/player/{playerId}")
    public List<PlayerTransferResponse> getByPlayer(@PathVariable Long playerId) {
        return service.getByPlayer(playerId);
    }

    @GetMapping("/team/{teamId}")
    public List<PlayerTransferResponse> getByTeam(@PathVariable Long teamId) {
        return service.getByTeam(teamId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerTransferResponse create(
            @Valid @RequestBody CreatePlayerTransferRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public PlayerTransferResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePlayerTransferRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}