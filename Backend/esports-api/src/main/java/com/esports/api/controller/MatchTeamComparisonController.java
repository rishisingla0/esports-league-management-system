package com.esports.api.controller;

import com.esports.api.dto.MatchTeamComparisonResponse;
import com.esports.api.service.MatchTeamComparisonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics/matches")
public class MatchTeamComparisonController {

    private final MatchTeamComparisonService service;

    public MatchTeamComparisonController(
            MatchTeamComparisonService service) {
        this.service = service;
    }

    @GetMapping("/{matchId}/team-comparison")
    public List<MatchTeamComparisonResponse> getTeamComparison(
            @PathVariable Long matchId) {

        return service.getTeamComparison(matchId);
    }
}