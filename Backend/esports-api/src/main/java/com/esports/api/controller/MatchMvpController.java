package com.esports.api.controller;

import com.esports.api.dto.MatchMvpResponse;
import com.esports.api.service.MatchMvpService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics/matches")
public class MatchMvpController {

    private final MatchMvpService service;

    public MatchMvpController(MatchMvpService service) {
        this.service = service;
    }

    @GetMapping("/{matchId}/mvp")
    public List<MatchMvpResponse> getMvp(
            @PathVariable Long matchId) {

        return service.getMvp(matchId);
    }
}