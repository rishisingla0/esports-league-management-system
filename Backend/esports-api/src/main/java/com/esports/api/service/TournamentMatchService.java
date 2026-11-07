package com.esports.api.service;

import com.esports.api.dto.TournamentMatchResponse;
import com.esports.api.repository.TournamentMatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TournamentMatchService {

    private final TournamentMatchRepository repository;

    public TournamentMatchService(TournamentMatchRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TournamentMatchResponse> getTournamentMatches(Long tournamentId) {
        return repository.getTournamentMatches(tournamentId);
    }
}