package com.esports.api.controller;

import com.esports.api.dto.TournamentStandingResponse;
import com.esports.api.service.TournamentStandingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics/tournaments")
public class TournamentStandingController {

    private final TournamentStandingService service;

    public TournamentStandingController(TournamentStandingService service) {
        this.service = service;
    }

    @GetMapping("/{tournamentId}/standings")
    public List<TournamentStandingResponse> getStandings(
            @PathVariable Long tournamentId) {
        return service.getStandings(tournamentId);
    }
}