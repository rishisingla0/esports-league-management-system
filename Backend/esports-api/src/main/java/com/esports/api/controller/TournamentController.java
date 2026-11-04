package com.esports.api.controller;

import com.esports.api.dto.CreateTournamentRequest;
import com.esports.api.dto.TournamentResponse;
import com.esports.api.service.TournamentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping
    public List<TournamentResponse> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping("/{id}")
    public TournamentResponse getTournamentById(
            @PathVariable Long id) {
        return tournamentService.getTournamentById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TournamentResponse createTournament(
            @Valid @RequestBody CreateTournamentRequest request) {
        return tournamentService.createTournament(request);
    }

    @PutMapping("/{id}")
    public TournamentResponse updateTournament(
            @PathVariable Long id,
            @Valid @RequestBody CreateTournamentRequest request) {
        return tournamentService.updateTournament(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
    }
}