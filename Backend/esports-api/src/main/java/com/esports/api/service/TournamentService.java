package com.esports.api.service;

import com.esports.api.dto.CreateTournamentRequest;
import com.esports.api.dto.TournamentResponse;
import com.esports.api.entity.Game;
import com.esports.api.entity.Tournament;
import com.esports.api.repository.GameRepository;
import com.esports.api.repository.TournamentRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final GameRepository gameRepository;

    public TournamentService(
            TournamentRepository tournamentRepository,
            GameRepository gameRepository) {
        this.tournamentRepository = tournamentRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional(readOnly = true)
    public List<TournamentResponse> getAllTournaments() {
        return tournamentRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TournamentResponse getTournamentById(Long id) {
        return convertToResponse(findTournament(id));
    }

    @Transactional
    public TournamentResponse createTournament(
            CreateTournamentRequest request) {

        validateDates(request);

        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Game not found"));

        Tournament tournament = new Tournament();

        tournament.setGame(game);
        applyRequest(tournament, request);

        return convertToResponse(
                tournamentRepository.save(tournament));
    }

    @Transactional
    public TournamentResponse updateTournament(
            Long id, CreateTournamentRequest request) {

        Tournament tournament = findTournament(id);

        validateDates(request);

        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Game not found"));

        tournament.setGame(game);
        applyRequest(tournament, request);

        return convertToResponse(
                tournamentRepository.save(tournament));
    }

    @Transactional
    public void deleteTournament(Long id) {
        Tournament tournament = findTournament(id);
        tournamentRepository.delete(tournament);
    }

    private Tournament findTournament(Long id) {
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tournament not found with ID: " + id));
    }

    private void validateDates(CreateTournamentRequest request) {
        if (request.startDate() != null
                && request.endDate() != null
                && request.endDate().isBefore(request.startDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date cannot be before start date");
        }
    }

    private void applyRequest(
            Tournament tournament,
            CreateTournamentRequest request) {

        tournament.setTournamentName(request.tournamentName().trim());
        tournament.setStartDate(request.startDate());
        tournament.setEndDate(request.endDate());

        tournament.setPrizePool(
                request.prizePool() == null
                        ? BigDecimal.ZERO
                        : request.prizePool());

        tournament.setStatus(
                request.status() == null
                        ? "UPCOMING"
                        : request.status().trim().toUpperCase());
    }

    private TournamentResponse convertToResponse(
            Tournament tournament) {

        return new TournamentResponse(
                tournament.getTournamentId(),
                tournament.getGame().getGameId(),
                tournament.getGame().getGameName(),
                tournament.getTournamentName(),
                tournament.getStartDate(),
                tournament.getEndDate(),
                tournament.getPrizePool(),
                tournament.getStatus()
        );
    }
}