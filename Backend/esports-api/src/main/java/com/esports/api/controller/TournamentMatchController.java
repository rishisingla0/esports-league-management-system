package com.esports.api.controller;

import com.esports.api.dto.TournamentMatchResponse;
import com.esports.api.service.TournamentMatchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics/tournaments")
public class TournamentMatchController {

    private final TournamentMatchService service;

    public TournamentMatchController(TournamentMatchService service) {
        this.service = service;
    }

    @GetMapping("/{tournamentId}/matches")
    public List<TournamentMatchResponse> getTournamentMatches(
            @PathVariable Long tournamentId) {
        return service.getTournamentMatches(tournamentId);
    }
}