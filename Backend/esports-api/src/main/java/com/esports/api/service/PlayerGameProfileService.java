package com.esports.api.service;

import com.esports.api.dto.CreatePlayerGameProfileRequest;
import com.esports.api.dto.PlayerGameProfileResponse;
import com.esports.api.entity.*;
import com.esports.api.repository.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PlayerGameProfileService {

    private final PlayerGameProfileRepository profileRepository;
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;

    public PlayerGameProfileService(
            PlayerGameProfileRepository profileRepository,
            PlayerRepository playerRepository,
            GameRepository gameRepository) {
        this.profileRepository = profileRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional(readOnly = true)
    public List<PlayerGameProfileResponse> getAllProfiles() {
        return profileRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlayerGameProfileResponse getProfileById(Long id) {
        PlayerGameProfile profile = findProfile(id);
        return convertToResponse(profile);
    }

    @Transactional
    public PlayerGameProfileResponse createProfile(
            CreatePlayerGameProfileRequest request) {

        Player player = playerRepository.findById(request.playerId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Player not found"));

        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Game not found"));

        String inGameId = request.inGameId().trim();

        if (profileRepository.existsByPlayerPlayerIdAndGameGameId(
                request.playerId(), request.gameId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Player already has a profile for this game");
        }

        if (profileRepository.existsByGameGameIdAndInGameIdIgnoreCase(
                request.gameId(), inGameId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This in-game ID is already registered for this game");
        }

        PlayerGameProfile profile = new PlayerGameProfile();
        profile.setPlayer(player);
        profile.setGame(game);
        profile.setInGameId(inGameId);

        return convertToResponse(profileRepository.save(profile));
    }

    @Transactional
    public PlayerGameProfileResponse updateProfile(
            Long id, CreatePlayerGameProfileRequest request) {

        PlayerGameProfile profile = findProfile(id);

        Player player = playerRepository.findById(request.playerId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Player not found"));

        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Game not found"));

        String inGameId = request.inGameId().trim();

        if (profileRepository
                .existsByPlayerPlayerIdAndGameGameIdAndProfileIdNot(
                        request.playerId(), request.gameId(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Player already has a profile for this game");
        }

        if (profileRepository
                .existsByGameGameIdAndInGameIdIgnoreCaseAndProfileIdNot(
                        request.gameId(), inGameId, id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This in-game ID is already registered for this game");
        }

        profile.setPlayer(player);
        profile.setGame(game);
        profile.setInGameId(inGameId);

        return convertToResponse(profileRepository.save(profile));
    }

    @Transactional
    public void deleteProfile(Long id) {
        PlayerGameProfile profile = findProfile(id);
        profileRepository.delete(profile);
    }

    private PlayerGameProfile findProfile(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Player game profile not found with ID: " + id));
    }

    private PlayerGameProfileResponse convertToResponse(
            PlayerGameProfile profile) {

        return new PlayerGameProfileResponse(
                profile.getProfileId(),
                profile.getPlayer().getPlayerId(),
                profile.getPlayer().getPlayerName(),
                profile.getGame().getGameId(),
                profile.getGame().getGameName(),
                profile.getInGameId()
        );
    }
}