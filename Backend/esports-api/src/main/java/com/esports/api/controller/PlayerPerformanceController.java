package com.esports.api.controller;

import com.esports.api.dto.PlayerPerformanceRequest;
import com.esports.api.service.PlayerPerformanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/player-performances")
public class PlayerPerformanceController {

    private final PlayerPerformanceService service;

    public PlayerPerformanceController(PlayerPerformanceService service) {
        this.service = service;
    }

    @GetMapping
    public List<Map<String, Object>> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/match/{matchId}")
    public List<Map<String, Object>> getByMatch(
            @PathVariable Long matchId) {
        return service.getByMatch(matchId);
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> create(
            @Valid @RequestBody PlayerPerformanceRequest request) {

        Long id = service.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("performanceId", id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @Valid @RequestBody PlayerPerformanceRequest request) {

        service.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}