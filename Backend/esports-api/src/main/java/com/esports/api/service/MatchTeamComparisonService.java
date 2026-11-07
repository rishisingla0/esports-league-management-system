package com.esports.api.service;

import com.esports.api.dto.MatchTeamComparisonResponse;
import com.esports.api.repository.MatchTeamComparisonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchTeamComparisonService {

    private final MatchTeamComparisonRepository repository;

    public MatchTeamComparisonService(
            MatchTeamComparisonRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MatchTeamComparisonResponse> getTeamComparison(Long matchId) {
        return repository.getTeamComparison(matchId);
    }
}