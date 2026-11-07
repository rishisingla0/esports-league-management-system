package com.esports.api.controller;

import com.esports.api.dto.TournamentOverviewResponse;
import com.esports.api.service.TournamentAnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics/tournaments")
public class TournamentAnalyticsController {

    private final TournamentAnalyticsService service;

    public TournamentAnalyticsController(TournamentAnalyticsService service) {
        this.service = service;
    }

    @GetMapping
    public List<TournamentOverviewResponse> getTournamentOverview() {
        return service.getTournamentOverview();
    }
}