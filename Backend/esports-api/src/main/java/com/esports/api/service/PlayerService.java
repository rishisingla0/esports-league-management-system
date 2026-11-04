package com.esports.api.service;

import com.esports.api.dto.CreatePlayerRequest;
import com.esports.api.dto.PlayerResponse;
import com.esports.api.entity.Player;
import com.esports.api.repository.PlayerRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // GET: Retrieve all players
    @Transactional(readOnly = true)
    public List<PlayerResponse> getAllPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET: Retrieve player by ID
    @Transactional(readOnly = true)
    public PlayerResponse getPlayerById(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Player not found with ID: " + id));

        return convertToResponse(player);
    }

    // POST: Create player
    @Transactional
    public PlayerResponse createPlayer(CreatePlayerRequest request) {
        Player player = new Player();

        player.setPlayerName(request.playerName().trim());
        player.setCountry(request.country());
        player.setDateOfBirth(request.dateOfBirth());

        Player savedPlayer = playerRepository.save(player);

        return convertToResponse(savedPlayer);
    }

    // PUT: Update player
    @Transactional
    public PlayerResponse updatePlayer(
            Long id, CreatePlayerRequest request) {

        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Player not found with ID: " + id));

        player.setPlayerName(request.playerName().trim());
        player.setCountry(request.country());
        player.setDateOfBirth(request.dateOfBirth());

        Player updatedPlayer = playerRepository.save(player);

        return convertToResponse(updatedPlayer);
    }

    // DELETE: Delete player
    @Transactional
    public void deletePlayer(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Player not found with ID: " + id));

        playerRepository.delete(player);
    }

    // Convert Entity into Response DTO
    private PlayerResponse convertToResponse(Player player) {
        return new PlayerResponse(
                player.getPlayerId(),
                player.getPlayerName(),
                player.getCountry(),
                player.getDateOfBirth()
        );
    }
}