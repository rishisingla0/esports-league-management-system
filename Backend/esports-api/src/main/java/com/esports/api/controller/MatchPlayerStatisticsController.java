package com.esports.api.controller;

import com.esports.api.dto.MatchPlayerStatisticsResponse;
import com.esports.api.service.MatchPlayerStatisticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics/matches")
public class MatchPlayerStatisticsController {

    private final MatchPlayerStatisticsService service;

    public MatchPlayerStatisticsController(
            MatchPlayerStatisticsService service) {
        this.service = service;
    }

    @GetMapping("/{matchId}/player-statistics")
    public List<MatchPlayerStatisticsResponse> getPlayerStatistics(
            @PathVariable Long matchId) {

        return service.getPlayerStatistics(matchId);
    }
}