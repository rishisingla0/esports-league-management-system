package com.esports.api.service;

import com.esports.api.dto.LeaderboardResponse;
import com.esports.api.repository.LeaderboardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;

    public LeaderboardService(LeaderboardRepository leaderboardRepository) {
        this.leaderboardRepository = leaderboardRepository;
    }

    @Transactional(readOnly = true)
    public List<LeaderboardResponse> getTopKills() {
        return leaderboardRepository.getTopKills();
    }

    @Transactional(readOnly = true)
    public List<LeaderboardResponse> getTopDamage() {
        return leaderboardRepository.getTopDamage();
    }

    @Transactional(readOnly = true)
    public List<LeaderboardResponse> getTopAssists() {
        return leaderboardRepository.getTopAssists();
    }
}