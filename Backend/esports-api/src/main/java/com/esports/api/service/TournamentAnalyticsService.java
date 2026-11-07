package com.esports.api.service;

import com.esports.api.dto.TournamentOverviewResponse;
import com.esports.api.repository.TournamentAnalyticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TournamentAnalyticsService {

    private final TournamentAnalyticsRepository repository;

    public TournamentAnalyticsService(TournamentAnalyticsRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TournamentOverviewResponse> getTournamentOverview() {
        return repository.getTournamentOverview();
    }
}