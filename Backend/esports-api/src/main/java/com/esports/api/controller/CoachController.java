
package com.esports.api.controller;

import com.esports.api.dto.*;
import com.esports.api.service.CoachService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coaches")
public class CoachController {

    private final CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping
    public List<CoachResponse> getAllCoaches() {
        return coachService.getAllCoaches();
    }

    @GetMapping("/{id}")
    public CoachResponse getCoachById(@PathVariable Long id) {
        return coachService.getCoachById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CoachResponse createCoach(
            @Valid @RequestBody CreateCoachRequest request) {
        return coachService.createCoach(request);
    }

    @PutMapping("/{id}")
    public CoachResponse updateCoach(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCoachRequest request) {
        return coachService.updateCoach(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCoach(@PathVariable Long id) {
        coachService.deleteCoach(id);
    }
}
