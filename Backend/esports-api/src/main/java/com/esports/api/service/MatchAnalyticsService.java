package com.esports.api.service;

import com.esports.api.dto.MatchOverviewResponse;
import com.esports.api.repository.MatchAnalyticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchAnalyticsService {

    private final MatchAnalyticsRepository matchAnalyticsRepository;

    public MatchAnalyticsService(
            MatchAnalyticsRepository matchAnalyticsRepository) {
        this.matchAnalyticsRepository = matchAnalyticsRepository;
    }

    @Transactional(readOnly = true)
    public List<MatchOverviewResponse> getMatchOverview() {
        return matchAnalyticsRepository.getMatchOverview();
    }
}