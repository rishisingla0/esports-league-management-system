package com.esports.api.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/api/health")
    public Map<String, Object> health() {
        Integer gameCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM games",
            Integer.class
        );

        return Map.of(
            "status", "UP",
            "database", "CONNECTED",
            "gamesCount", gameCount
        );
    }
}
