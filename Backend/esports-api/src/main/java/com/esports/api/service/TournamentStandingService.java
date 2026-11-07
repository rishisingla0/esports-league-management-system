package com.esports.api.service;

import com.esports.api.dto.TournamentStandingResponse;
import com.esports.api.repository.TournamentStandingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TournamentStandingService {

    private final TournamentStandingRepository repository;

    public TournamentStandingService(TournamentStandingRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TournamentStandingResponse> getStandings(Long tournamentId) {
        return repository.getStandings(tournamentId);
    }
}