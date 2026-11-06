package com.esports.api.service;

import com.esports.api.dto.*;
import com.esports.api.entity.TeamSponsorship;
import com.esports.api.repository.TeamSponsorshipRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TeamSponsorshipService {

    private final TeamSponsorshipRepository repository;

    public TeamSponsorshipService(TeamSponsorshipRepository repository) {
        this.repository = repository;
    }

    public List<TeamSponsorshipResponse> getAll() {
        return repository.findAll().stream().map(this::mapToResponse).toList();
    }

    public TeamSponsorshipResponse getById(Long id) {
        return mapToResponse(findSponsorship(id));
    }

    public List<TeamSponsorshipResponse> getByTeam(Long teamId) {
        return repository.findByTeamIdOrderBySponsorshipIdAsc(teamId)
                .stream().map(this::mapToResponse).toList();
    }

    public List<TeamSponsorshipResponse> getBySponsor(Long sponsorId) {
        return repository.findBySponsorIdOrderBySponsorshipIdAsc(sponsorId)
                .stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public TeamSponsorshipResponse create(CreateTeamSponsorshipRequest request) {
        validate(request.startDate(), request.endDate(),
                request.sponsorshipAmount(), request.status());

        TeamSponsorship sponsorship = new TeamSponsorship();

        applyRequest(sponsorship, request.teamId(), request.sponsorId(),
                request.startDate(), request.endDate(),
                request.sponsorshipAmount(), request.status());

        try {
            return mapToResponse(repository.saveAndFlush(sponsorship));
        } catch (DataAccessException ex) {
            throw conflict();
        }
    }

    @Transactional
    public TeamSponsorshipResponse update(Long id, UpdateTeamSponsorshipRequest request) {
        validate(request.startDate(), request.endDate(),
                request.sponsorshipAmount(), request.status());

        TeamSponsorship sponsorship = findSponsorship(id);

        applyRequest(sponsorship, request.teamId(), request.sponsorId(),
                request.startDate(), request.endDate(),
                request.sponsorshipAmount(), request.status());

        try {
            return mapToResponse(repository.saveAndFlush(sponsorship));
        } catch (DataAccessException ex) {
            throw conflict();
        }
    }

    @Transactional
    public void delete(Long id) {
        TeamSponsorship sponsorship = findSponsorship(id);

        try {
            repository.delete(sponsorship);
            repository.flush();
        } catch (DataAccessException ex) {
            throw conflict();
        }
    }

    private TeamSponsorship findSponsorship(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Team sponsorship not found"));
    }

    private void validate(LocalDate startDate, LocalDate endDate,
                          BigDecimal amount, String status) {

        if (endDate != null && endDate.isBefore(startDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date cannot be earlier than start date");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Sponsorship amount cannot be negative");
        }

        if (!List.of("ACTIVE", "COMPLETED", "CANCELLED").contains(status)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid sponsorship status");
        }
    }

    private void applyRequest(TeamSponsorship sponsorship, Long teamId,
                              Long sponsorId, LocalDate startDate,
                              LocalDate endDate, BigDecimal amount, String status) {

        sponsorship.setTeamId(teamId);
        sponsorship.setSponsorId(sponsorId);
        sponsorship.setStartDate(startDate);
        sponsorship.setEndDate(endDate);
        sponsorship.setSponsorshipAmount(amount);
        sponsorship.setStatus(status);
    }

    private TeamSponsorshipResponse mapToResponse(TeamSponsorship sponsorship) {
        return new TeamSponsorshipResponse(
                sponsorship.getSponsorshipId(),
                sponsorship.getTeamId(),
                sponsorship.getSponsorId(),
                sponsorship.getStartDate(),
                sponsorship.getEndDate(),
                sponsorship.getSponsorshipAmount(),
                sponsorship.getStatus()
        );
    }

    private ResponseStatusException conflict() {
        return new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Team sponsorship operation violates a database constraint");
    }
}