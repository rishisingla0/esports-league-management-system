package com.esports.api.service;

import com.esports.api.dto.PlayerPerformanceRequest;
import com.esports.api.repository.PlayerPerformanceRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class PlayerPerformanceService {

    private final PlayerPerformanceRepository repository;

    public PlayerPerformanceService(PlayerPerformanceRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAll() {
        return repository.getAll();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getById(Long id) {
        List<Map<String, Object>> result = repository.getById(id);

        if (result.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Performance not found"
            );
        }

        return result.get(0);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getByMatch(Long matchId) {
        return repository.getByMatch(matchId);
    }

    @Transactional
    public Long create(PlayerPerformanceRequest request) {
        try {
            return repository.create(request);
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Could not create performance. Check match eligibility and duplicate records."
            );
        }
    }

    @Transactional
    public void update(Long id, PlayerPerformanceRequest request) {
        try {
            int updated = repository.update(id, request);

            if (updated == 0) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Performance not found"
                );
            }
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Could not update performance. Check match status and constraints."
            );
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            int deleted = repository.delete(id);

            if (deleted == 0) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Performance not found"
                );
            }
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Could not delete performance. Check match status."
            );
        }
    }
}