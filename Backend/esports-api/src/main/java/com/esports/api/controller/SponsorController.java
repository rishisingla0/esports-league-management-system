package com.esports.api.controller;

import com.esports.api.dto.*;
import com.esports.api.service.SponsorService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sponsors")
public class SponsorController {

    private final SponsorService service;

    public SponsorController(SponsorService service) {
        this.service = service;
    }

    @GetMapping
    public List<SponsorResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public SponsorResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SponsorResponse create(@Valid @RequestBody CreateSponsorRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public SponsorResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSponsorRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}