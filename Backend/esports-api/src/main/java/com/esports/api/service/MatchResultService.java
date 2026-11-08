package com.esports.api.service;

import com.esports.api.dto.MatchResultRequest;
import com.esports.api.repository.MatchResultRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class MatchResultService {

    private final MatchResultRepository repository;

    public MatchResultService(MatchResultRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getResults(Long matchId) {
        return repository.getResults(matchId);
    }

    @Transactional
    public void updateResult(Long matchTeamId, MatchResultRequest request) {

        int updated = repository.updateResult(matchTeamId, request);

        if (updated == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Match team record not found"
            );
        }
    }
}