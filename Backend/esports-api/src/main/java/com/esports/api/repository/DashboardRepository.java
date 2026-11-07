package com.esports.api.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class DashboardRepository {

    private final JdbcTemplate jdbcTemplate;

    public DashboardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long countGames() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM games", Long.class);
    }

    public long countTeams() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM teams", Long.class);
    }

    public long countPlayers() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM players", Long.class);
    }

    public long countOrganizations() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM organizations", Long.class);
    }

    public long countTournaments() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tournaments", Long.class);
    }

    public long countOngoingMatches() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM matches WHERE status = 'ONGOING'",
                Long.class);
    }

    public long countScheduledMatches() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM matches WHERE status = 'SCHEDULED'",
                Long.class);
    }

    public BigDecimal getTotalPrizeMoneyAwarded() {
        return jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(prize_amount), 0) FROM prize_distributions",
                BigDecimal.class);
    }

    public BigDecimal getActiveSponsorshipValue() {
        return jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(sponsorship_amount), 0) FROM team_sponsorships WHERE status = 'ACTIVE'",
                BigDecimal.class);
    }
}