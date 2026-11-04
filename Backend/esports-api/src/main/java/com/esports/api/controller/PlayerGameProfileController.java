package com.esports.api.controller;

import com.esports.api.dto.CreatePlayerGameProfileRequest;
import com.esports.api.dto.PlayerGameProfileResponse;
import com.esports.api.service.PlayerGameProfileService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player-game-profiles")
public class PlayerGameProfileController {

    private final PlayerGameProfileService profileService;

    public PlayerGameProfileController(
            PlayerGameProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public List<PlayerGameProfileResponse> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public PlayerGameProfileResponse getProfileById(
            @PathVariable Long id) {
        return profileService.getProfileById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerGameProfileResponse createProfile(
            @Valid @RequestBody CreatePlayerGameProfileRequest request) {
        return profileService.createProfile(request);
    }

    @PutMapping("/{id}")
    public PlayerGameProfileResponse updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody CreatePlayerGameProfileRequest request) {
        return profileService.updateProfile(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
    }
}