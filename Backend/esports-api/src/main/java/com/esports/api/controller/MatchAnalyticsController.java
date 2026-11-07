package com.esports.api.controller;

import com.esports.api.dto.MatchOverviewResponse;
import com.esports.api.service.MatchAnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics/matches")
public class MatchAnalyticsController {

    private final MatchAnalyticsService matchAnalyticsService;

    public MatchAnalyticsController(
            MatchAnalyticsService matchAnalyticsService) {
        this.matchAnalyticsService = matchAnalyticsService;
    }

    @GetMapping
    public List<MatchOverviewResponse> getMatchOverview() {
        return matchAnalyticsService.getMatchOverview();
    }
}