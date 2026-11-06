package com.esports.api.service;

import com.esports.api.dto.*;
import com.esports.api.entity.TeamRoster;
import com.esports.api.repository.TeamRosterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataAccessException;
import java.util.List;

@Service
public class TeamRosterService {

    private final TeamRosterRepository repository;

    public TeamRosterService(TeamRosterRepository repository) {
        this.repository = repository;
    }

    public List<TeamRosterResponse> getAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse).toList();
    }

    public TeamRosterResponse getById(Long id) {
        return mapToResponse(findRoster(id));
    }

    public List<TeamRosterResponse> getByTeam(Long teamId) {
        return repository.findByTeamIdOrderByRosterIdAsc(teamId)
                .stream().map(this::mapToResponse).toList();
    }

    public List<TeamRosterResponse> getByPlayer(Long playerId) {
        return repository.findByPlayerIdOrderByRosterIdAsc(playerId)
                .stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public TeamRosterResponse create(CreateTeamRosterRequest request) {
        validateDates(request.startDate(), request.endDate());

        TeamRoster roster = new TeamRoster();
        applyRequest(roster, request.teamId(), request.playerId(),
                request.role(), request.startDate(), request.endDate(),
                request.status());

        try {
            return mapToResponse(repository.saveAndFlush(roster));
        } catch (DataAccessException ex)


 {
            throw conflict();
        }
    }

    @Transactional
    public TeamRosterResponse update(Long id, UpdateTeamRosterRequest request) {
        validateDates(request.startDate(), request.endDate());

        TeamRoster roster = findRoster(id);

        applyRequest(roster, request.teamId(), request.playerId(),
                request.role(), request.startDate(), request.endDate(),
                request.status());

        try {
            return mapToResponse(repository.saveAndFlush(roster));
        } catch (DataAccessException ex)


 {
            throw conflict();
        }
    }

    @Transactional
    public void delete(Long id) {
        TeamRoster roster = findRoster(id);

        try {
            repository.delete(roster);
            repository.flush();
        } catch (DataAccessException ex)


 {
            throw conflict();
        }
    }

    private TeamRoster findRoster(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Team roster not found"));
    }

    private void validateDates(
            java.time.LocalDate startDate,
            java.time.LocalDate endDate) {

        if (endDate != null && endDate.isBefore(startDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date cannot be earlier than start date");
        }
    }

    private void applyRequest(
            TeamRoster roster,
            Long teamId,
            Long playerId,
            String role,
            java.time.LocalDate startDate,
            java.time.LocalDate endDate,
            String status) {

        roster.setTeamId(teamId);
        roster.setPlayerId(playerId);
        roster.setRole(role);
        roster.setStartDate(startDate);
        roster.setEndDate(endDate);
        roster.setStatus(status);
    }

    private ResponseStatusException conflict() {
        return new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Roster operation violates a database constraint or historical match protection");
    }

    private TeamRosterResponse mapToResponse(TeamRoster roster) {
        return new TeamRosterResponse(
                roster.getRosterId(),
                roster.getTeamId(),
                roster.getPlayerId(),
                roster.getRole(),
                roster.getStartDate(),
                roster.getEndDate(),
                roster.getStatus()
        );
    }
}