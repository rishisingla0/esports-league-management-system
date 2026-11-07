package com.esports.api.controller;

import com.esports.api.dto.LeaderboardResponse;
import com.esports.api.service.LeaderboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboards")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/top-kills")
    public List<LeaderboardResponse> getTopKills() {
        return leaderboardService.getTopKills();
    }

    @GetMapping("/top-damage")
    public List<LeaderboardResponse> getTopDamage() {
        return leaderboardService.getTopDamage();
    }

    @GetMapping("/top-assists")
    public List<LeaderboardResponse> getTopAssists() {
        return leaderboardService.getTopAssists();
    }
}