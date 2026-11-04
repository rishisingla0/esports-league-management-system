package com.esports.api.controller;

import com.esports.api.dto.CreatePlayerRequest;
import com.esports.api.dto.PlayerResponse;
import com.esports.api.service.PlayerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // GET: Retrieve all players
    @GetMapping
    public List<PlayerResponse> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    // GET: Retrieve player by ID
    @GetMapping("/{id}")
    public PlayerResponse getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id);
    }

    // POST: Create player
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerResponse createPlayer(
            @Valid @RequestBody CreatePlayerRequest request) {
        return playerService.createPlayer(request);
    }

    // PUT: Update player
    @PutMapping("/{id}")
    public PlayerResponse updatePlayer(
            @PathVariable Long id,
            @Valid @RequestBody CreatePlayerRequest request) {
        return playerService.updatePlayer(id, request);
    }

    // DELETE: Delete player
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }
}