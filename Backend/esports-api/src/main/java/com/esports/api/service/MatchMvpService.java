package com.esports.api.service;

import com.esports.api.dto.MatchMvpResponse;
import com.esports.api.repository.MatchMvpRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchMvpService {

    private final MatchMvpRepository repository;

    public MatchMvpService(MatchMvpRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MatchMvpResponse> getMvp(Long matchId) {
        return repository.getMvp(matchId);
    }
}