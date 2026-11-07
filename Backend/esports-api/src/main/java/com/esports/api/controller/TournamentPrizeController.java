package com.esports.api.controller;

import com.esports.api.dto.TournamentPrizeSummaryResponse;
import com.esports.api.service.TournamentPrizeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics/tournaments")
public class TournamentPrizeController {

    private final TournamentPrizeService service;

    public TournamentPrizeController(TournamentPrizeService service) {
        this.service = service;
    }

    @GetMapping("/{tournamentId}/prizes")
    public TournamentPrizeSummaryResponse getPrizeSummary(
            @PathVariable Long tournamentId) {
        return service.getPrizeSummary(tournamentId);
    }
}