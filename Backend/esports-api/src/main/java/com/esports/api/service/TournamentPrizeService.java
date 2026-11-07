package com.esports.api.service;

import com.esports.api.dto.TournamentPrizeSummaryResponse;
import com.esports.api.repository.TournamentPrizeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TournamentPrizeService {

    private final TournamentPrizeRepository repository;

    public TournamentPrizeService(TournamentPrizeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public TournamentPrizeSummaryResponse getPrizeSummary(Long tournamentId) {
        return repository.getPrizeSummary(tournamentId);
    }
}