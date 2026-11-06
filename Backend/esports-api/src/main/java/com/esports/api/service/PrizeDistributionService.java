package com.esports.api.service;

import com.esports.api.dto.*;
import com.esports.api.entity.PrizeDistribution;
import com.esports.api.repository.PrizeDistributionRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PrizeDistributionService {

    private final PrizeDistributionRepository repository;

    public PrizeDistributionService(PrizeDistributionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<PrizeDistributionResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PrizeDistributionResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public List<PrizeDistributionResponse> getByTournament(Long tournamentId) {
        return repository.findByTournamentIdOrderByPlacementAsc(tournamentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PrizeDistributionResponse> getByTeam(Long teamId) {
        return repository.findByTeamIdOrderByPrizeDistributionIdAsc(teamId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PrizeDistributionResponse create(CreatePrizeDistributionRequest request) {
        validate(request.tournamentId(), request.teamId(),
                request.placement(), request.prizeAmount());

        PrizeDistribution prize = new PrizeDistribution();
        applyRequest(prize, request.tournamentId(), request.teamId(),
                request.placement(), request.prizeAmount(), request.awardedAt());

        try {
            return toResponse(repository.saveAndFlush(prize));
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Prize distribution conflicts with an existing record or database constraint."
            );
        }
    }

    public PrizeDistributionResponse update(Long id, UpdatePrizeDistributionRequest request) {
        PrizeDistribution prize = findById(id);

        validate(request.tournamentId(), request.teamId(),
                request.placement(), request.prizeAmount());

        applyRequest(prize, request.tournamentId(), request.teamId(),
                request.placement(), request.prizeAmount(), request.awardedAt());

        try {
            return toResponse(repository.saveAndFlush(prize));
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Prize distribution conflicts with an existing record or database constraint."
            );
        }
    }

    public void delete(Long id) {
        PrizeDistribution prize = findById(id);
        repository.delete(prize);
        repository.flush();
    }

    private PrizeDistribution findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Prize distribution not found"
                ));
    }

    private void validate(Long tournamentId, Long teamId,
                          Integer placement, BigDecimal amount) {
        if (tournamentId == null || teamId == null || placement == null
                || amount == null || placement <= 0
                || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid prize distribution details"
            );
        }
    }

    private void applyRequest(PrizeDistribution prize, Long tournamentId,
                              Long teamId, Integer placement,
                              BigDecimal amount, java.time.LocalDate awardedAt) {
        prize.setTournamentId(tournamentId);
        prize.setTeamId(teamId);
        prize.setPlacement(placement);
        prize.setPrizeAmount(amount);
        prize.setAwardedAt(awardedAt);
    }

    private PrizeDistributionResponse toResponse(PrizeDistribution prize) {
        return new PrizeDistributionResponse(
                prize.getPrizeDistributionId(),
                prize.getTournamentId(),
                prize.getTeamId(),
                prize.getPlacement(),
                prize.getPrizeAmount(),
                prize.getAwardedAt()
        );
    }
}