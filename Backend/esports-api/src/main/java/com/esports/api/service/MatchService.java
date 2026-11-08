package com.esports.api.service;

import com.esports.api.dto.*;
import com.esports.api.entity.*;
import com.esports.api.repository.*;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;

    public MatchService(
            MatchRepository matchRepository,
            TournamentRepository tournamentRepository) {

        this.matchRepository = matchRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional(readOnly = true)
    public List<MatchResponse> getAllMatches() {

        return matchRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MatchResponse getMatchById(Long id) {

        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Match not found"
                ));

        return mapToResponse(match);
    }

    @Transactional
    public MatchResponse createMatch(CreateMatchRequest request) {

        Tournament tournament = tournamentRepository
                .findById(request.tournamentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tournament not found"
                ));

        if (matchRepository
                .existsByTournamentTournamentIdAndMatchNumber(
                        request.tournamentId(),
                        request.matchNumber())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This match number already exists in this tournament"
            );
        }

        Match match = new Match();

        match.setTournament(tournament);
        match.setMatchNumber(request.matchNumber());
        match.setMapName(request.mapName().trim());
        match.setScheduledAt(request.scheduledAt());

        // Every new match must start as SCHEDULED.
        match.setStatus("SCHEDULED");

        try {
            return mapToResponse(
                    matchRepository.saveAndFlush(match)
            );

        } catch (DataIntegrityViolationException ex) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    getDatabaseError(ex),
                    ex
            );
        }
    }

    @Transactional
    public MatchResponse updateMatch(
            Long id,
            UpdateMatchRequest request) {

        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Match not found"
                ));

        Tournament tournament = tournamentRepository
                .findById(request.tournamentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tournament not found"
                ));

        if (matchRepository
                .existsByTournamentTournamentIdAndMatchNumberAndMatchIdNot(
                        request.tournamentId(),
                        request.matchNumber(),
                        id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This match number already exists in this tournament"
            );
        }

        match.setTournament(tournament);
        match.setMatchNumber(request.matchNumber());
        match.setMapName(request.mapName().trim());
        match.setScheduledAt(request.scheduledAt());
        String newStatus = request.status().trim().toUpperCase();

if ("COMPLETED".equals(newStatus)) {
    throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Use the complete match endpoint to complete a match"
    );
}

match.setStatus(newStatus);

        try {
            return mapToResponse(
                    matchRepository.saveAndFlush(match)
            );

        } catch (DataIntegrityViolationException ex) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    getDatabaseError(ex),
                    ex
            );
        }
    }

    @Transactional
    public void deleteMatch(Long id) {

        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Match not found"
                ));

        try {
            matchRepository.delete(match);
            matchRepository.flush();

        } catch (DataIntegrityViolationException ex) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete this match because it has related records",
                    ex
            );
        }
    }

    private String getDatabaseError(
            DataIntegrityViolationException ex) {

        Throwable cause = ex;

        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        String message = cause.getMessage();

        if (message != null &&
                message.contains("A match must be created as SCHEDULED")) {

            return "A new match must be created with SCHEDULED status";
        }

        if (message != null &&
                message.contains("Completed or cancelled matches cannot be reopened")) {

            return "Completed or cancelled matches cannot be reopened";
        }

        if (message != null &&
                message.contains("A match must have participating teams")) {

            return "Add participating teams before starting the match";
        }

        if (message != null &&
                message.contains("Every participating team must have exactly four players")) {

            return "Every participating team must have exactly four players before starting the match";
        }

        return "Match operation violates a database constraint";
    }

    private MatchResponse mapToResponse(Match match) {

        return new MatchResponse(
                match.getMatchId(),
                match.getTournament().getTournamentId(),
                match.getTournament().getTournamentName(),
                match.getMatchNumber(),
                match.getMapName(),
                match.getScheduledAt(),
                match.getStatus()
        );
    }
}