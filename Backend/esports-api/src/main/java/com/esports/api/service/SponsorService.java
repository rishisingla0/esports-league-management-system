package com.esports.api.service;

import com.esports.api.dto.*;
import com.esports.api.entity.Sponsor;
import com.esports.api.repository.SponsorRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SponsorService {

    private final SponsorRepository repository;

    public SponsorService(SponsorRepository repository) {
        this.repository = repository;
    }

    public List<SponsorResponse> getAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse).toList();
    }

    public SponsorResponse getById(Long id) {
        return mapToResponse(findSponsor(id));
    }

    @Transactional
    public SponsorResponse create(CreateSponsorRequest request) {
        Sponsor sponsor = new Sponsor();
        applyRequest(sponsor, request.sponsorName(),
                request.industry(), request.country(), request.website());

        try {
            return mapToResponse(repository.saveAndFlush(sponsor));
        } catch (DataAccessException ex) {
            throw conflict();
        }
    }

    @Transactional
    public SponsorResponse update(Long id, UpdateSponsorRequest request) {
        Sponsor sponsor = findSponsor(id);

        applyRequest(sponsor, request.sponsorName(),
                request.industry(), request.country(), request.website());

        try {
            return mapToResponse(repository.saveAndFlush(sponsor));
        } catch (DataAccessException ex) {
            throw conflict();
        }
    }

    @Transactional
    public void delete(Long id) {
        Sponsor sponsor = findSponsor(id);

        try {
            repository.delete(sponsor);
            repository.flush();
        } catch (DataAccessException ex) {
            throw conflict();
        }
    }

    private Sponsor findSponsor(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sponsor not found"));
    }

    private void applyRequest(Sponsor sponsor, String name,
                              String industry, String country, String website) {
        sponsor.setSponsorName(name);
        sponsor.setIndustry(industry);
        sponsor.setCountry(country);
        sponsor.setWebsite(website);
    }

    private SponsorResponse mapToResponse(Sponsor sponsor) {
        return new SponsorResponse(
                sponsor.getSponsorId(),
                sponsor.getSponsorName(),
                sponsor.getIndustry(),
                sponsor.getCountry(),
                sponsor.getWebsite()
        );
    }

    private ResponseStatusException conflict() {
        return new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Sponsor name already exists or operation violates a database constraint");
    }
}