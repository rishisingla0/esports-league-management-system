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
public class TournamentRegistrationService {

    private final TournamentRegistrationRepository registrationRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;

    public TournamentRegistrationService(
            TournamentRegistrationRepository registrationRepository,
            TournamentRepository tournamentRepository,
            TeamRepository teamRepository) {

        this.registrationRepository = registrationRepository;
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional(readOnly = true)
    public List<TournamentRegistrationResponse> getAllRegistrations() {
        return registrationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TournamentRegistrationResponse getRegistrationById(Long id) {

        TournamentRegistration registration = registrationRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tournament registration not found"
                ));

        return mapToResponse(registration);
    }

    @Transactional
    public TournamentRegistrationResponse createRegistration(
            CreateTournamentRegistrationRequest request) {

        Tournament tournament = tournamentRepository
                .findById(request.tournamentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tournament not found"
                ));

        Team team = teamRepository
                .findById(request.teamId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team not found"
                ));

        validateSameGame(tournament, team);

        if (registrationRepository
                .existsByTournamentTournamentIdAndTeamTeamId(
                        request.tournamentId(),
                        request.teamId())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This team is already registered for this tournament"
            );
        }

        TournamentRegistration registration =
                new TournamentRegistration();

        registration.setTournament(tournament);
        registration.setTeam(team);
        registration.setRegistrationDate(request.registrationDate());
        registration.setSeed(request.seed());
        registration.setStatus(normalizeStatus(request.status()));

        try {
            return mapToResponse(
                    registrationRepository.saveAndFlush(registration)
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
    public TournamentRegistrationResponse updateRegistration(
            Long id,
            UpdateTournamentRegistrationRequest request) {

        TournamentRegistration registration = registrationRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tournament registration not found"
                ));

        Tournament tournament = tournamentRepository
                .findById(request.tournamentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tournament not found"
                ));

        Team team = teamRepository
                .findById(request.teamId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team not found"
                ));

        validateSameGame(tournament, team);

        if (registrationRepository
                .existsByTournamentTournamentIdAndTeamTeamIdAndRegistrationIdNot(
                        request.tournamentId(),
                        request.teamId(),
                        id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This team is already registered for this tournament"
            );
        }

        registration.setTournament(tournament);
        registration.setTeam(team);
        registration.setRegistrationDate(request.registrationDate());
        registration.setSeed(request.seed());
        registration.setStatus(normalizeStatus(request.status()));

        try {
            return mapToResponse(
                    registrationRepository.saveAndFlush(registration)
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
    public void deleteRegistration(Long id) {

        TournamentRegistration registration = registrationRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tournament registration not found"
                ));

        try {
            registrationRepository.delete(registration);
            registrationRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    getDatabaseError(ex),
                    ex
            );
        }
    }

    private void validateSameGame(Tournament tournament, Team team) {

        if (!tournament.getGame().getGameId()
                .equals(team.getGame().getGameId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Team and tournament must belong to the same game"
            );
        }
    }

    private String normalizeStatus(String status) {

        if (status == null || status.isBlank()) {
            return "REGISTERED";
        }

        return status.trim().toUpperCase();
    }

    private String getDatabaseError(DataIntegrityViolationException ex) {

        Throwable cause = ex;

        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        String message = cause.getMessage();

        if (message != null && message.contains("Cannot register a team retroactively")) {
            return "Cannot register a team after a match has started";
        }

        if (message != null && message.contains("Cannot modify registration relationships")) {
            return "Cannot modify registration relationships used by started matches";
        }

        if (message != null && message.contains("Registration used by a started match")) {
            return "Cannot delete a registration used by a started match";
        }

        return "Registration violates a database constraint";
    }

    private TournamentRegistrationResponse mapToResponse(
            TournamentRegistration registration) {

        return new TournamentRegistrationResponse(
                registration.getRegistrationId(),
                registration.getTournament().getTournamentId(),
                registration.getTournament().getTournamentName(),
                registration.getTeam().getTeamId(),
                registration.getTeam().getTeamName(),
                registration.getRegistrationDate(),
                registration.getSeed(),
                registration.getStatus()
        );
    }
}