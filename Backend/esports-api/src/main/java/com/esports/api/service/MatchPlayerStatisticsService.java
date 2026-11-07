package com.esports.api.service;

import com.esports.api.dto.MatchPlayerStatisticsResponse;
import com.esports.api.repository.MatchPlayerStatisticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchPlayerStatisticsService {

    private final MatchPlayerStatisticsRepository repository;

    public MatchPlayerStatisticsService(
            MatchPlayerStatisticsRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MatchPlayerStatisticsResponse> getPlayerStatistics(Long matchId) {
        return repository.getPlayerStatistics(matchId);
    }
}