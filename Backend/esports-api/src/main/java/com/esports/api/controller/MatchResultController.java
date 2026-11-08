package com.esports.api.controller;

import com.esports.api.dto.MatchResultRequest;
import com.esports.api.service.MatchResultService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/match-results")
public class MatchResultController {

    private final MatchResultService service;

    public MatchResultController(MatchResultService service) {
        this.service = service;
    }

    @GetMapping("/match/{matchId}")
    public List<Map<String, Object>> getResults(
            @PathVariable Long matchId) {
        return service.getResults(matchId);
    }

    @PutMapping("/{matchTeamId}")
    public ResponseEntity<Void> updateResult(
            @PathVariable Long matchTeamId,
            @Valid @RequestBody MatchResultRequest request) {

        service.updateResult(matchTeamId, request);
        return ResponseEntity.noContent().build();
    }
}