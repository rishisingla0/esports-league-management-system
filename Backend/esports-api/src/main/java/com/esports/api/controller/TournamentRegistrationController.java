package com.esports.api.controller;

import com.esports.api.dto.*;
import com.esports.api.service.TournamentRegistrationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournament-registrations")
public class TournamentRegistrationController {

    private final TournamentRegistrationService registrationService;

    public TournamentRegistrationController(
            TournamentRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public ResponseEntity<List<TournamentRegistrationResponse>> getAll() {

        return ResponseEntity.ok(
                registrationService.getAllRegistrations()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentRegistrationResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                registrationService.getRegistrationById(id)
        );
    }

    @PostMapping
    public ResponseEntity<TournamentRegistrationResponse> create(
            @Valid @RequestBody CreateTournamentRegistrationRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registrationService.createRegistration(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TournamentRegistrationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTournamentRegistrationRequest request) {

        return ResponseEntity.ok(
                registrationService.updateRegistration(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        registrationService.deleteRegistration(id);

        return ResponseEntity.noContent().build();
    }
}