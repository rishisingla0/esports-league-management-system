package com.esports.api.service;

import com.esports.api.dto.CreatePlayerPerformanceRequest;
import com.esports.api.dto.UpdatePlayerPerformanceRequest;
import com.esports.api.dto.PlayerPerformanceResponse;
import com.esports.api.entity.MatchSquadPlayer;
import com.esports.api.entity.MatchTeam;
import com.esports.api.entity.Player;
import com.esports.api.entity.PlayerPerformance;
import com.esports.api.repository.MatchSquadPlayerRepository;
import com.esports.api.repository.PlayerPerformanceRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class PlayerPerformanceService {

    private final PlayerPerformanceRepository performanceRepository;
    private final MatchSquadPlayerRepository squadPlayerRepository;

    public PlayerPerformanceService(
            PlayerPerformanceRepository performanceRepository,
            MatchSquadPlayerRepository squadPlayerRepository) {

        this.performanceRepository = performanceRepository;
        this.squadPlayerRepository = squadPlayerRepository;
    }

    // Get all performance records
    @Transactional(readOnly = true)
    public List<PlayerPerformanceResponse> getAllPerformances() {
        return performanceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get performance by ID
    @Transactional(readOnly = true)
    public PlayerPerformanceResponse getPerformanceById(Long id) {

        PlayerPerformance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Performance record not found"
                ));

        return mapToResponse(performance);
    }

    // Get performances by match
    @Transactional(readOnly = true)
    public List<PlayerPerformanceResponse> getPerformancesByMatch(Long matchId) {

        return performanceRepository.findByMatchSquadPlayerMatchId(matchId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get performances by player
    @Transactional(readOnly = true)
    public List<PlayerPerformanceResponse> getPerformancesByPlayer(Long playerId) {

        return performanceRepository.findByMatchSquadPlayerPlayerPlayerId(playerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Create performance record
    public PlayerPerformanceResponse addPerformance(
            CreatePlayerPerformanceRequest request) {

        MatchSquadPlayer squadPlayer = squadPlayerRepository
                .findById(request.matchSquadPlayerId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Match squad player not found"
                ));

        if (performanceRepository.existsByMatchSquadPlayerMatchSquadPlayerId(
                request.matchSquadPlayerId())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Performance record already exists for this player in this match"
            );
        }

        PlayerPerformance performance = new PlayerPerformance();

        performance.setMatchSquadPlayer(squadPlayer);
        performance.setKills(request.kills());
        performance.setDamage(request.damage());
        performance.setSurvivalTimeSeconds(request.survivalTimeSeconds());
        performance.setAssists(request.assists());

        try {
            PlayerPerformance saved = performanceRepository.saveAndFlush(performance);
            return mapToResponse(saved);

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Performance record violates a database constraint or match-status rule"
            );
        }
    }

    // Update performance record
    public PlayerPerformanceResponse updatePerformance(
            Long id,
            UpdatePlayerPerformanceRequest request) {

        PlayerPerformance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Performance record not found"
                ));

        if (request.kills() != null) {
            performance.setKills(request.kills());
        }

        if (request.damage() != null) {
            performance.setDamage(request.damage());
        }

        if (request.survivalTimeSeconds() != null) {
            performance.setSurvivalTimeSeconds(request.survivalTimeSeconds());
        }

        if (request.assists() != null) {
            performance.setAssists(request.assists());
        }

        try {
            PlayerPerformance updated = performanceRepository.saveAndFlush(performance);
            return mapToResponse(updated);

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Performance update violates a database constraint or match-status rule"
            );
        }
    }

    // Delete performance record
    public void deletePerformance(Long id) {

        PlayerPerformance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Performance record not found"
                ));

        try {
            performanceRepository.delete(performance);
            performanceRepository.flush();

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Performance cannot be deleted due to a database constraint or match-status rule"
            );
        }
    }

    // Convert entity to response DTO
    private PlayerPerformanceResponse mapToResponse(
            PlayerPerformance performance) {

        MatchSquadPlayer squadPlayer = performance.getMatchSquadPlayer();
        MatchTeam matchTeam = squadPlayer.getMatchTeam();
        Player player = squadPlayer.getPlayer();

        return new PlayerPerformanceResponse(
                performance.getPerformanceId(),
                squadPlayer.getMatchSquadPlayerId(),
                squadPlayer.getMatchId(),
                matchTeam.getMatchTeamId(),
                matchTeam.getTeam().getTeamId(),
                matchTeam.getTeam().getTeamName(),
                player.getPlayerId(),
                player.getPlayerName(),
                performance.getKills(),
                performance.getDamage(),
                performance.getSurvivalTimeSeconds(),
                performance.getAssists()
        );
    }
}