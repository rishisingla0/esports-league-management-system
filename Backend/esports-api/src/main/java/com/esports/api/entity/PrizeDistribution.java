package com.esports.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "prize_distributions")
public class PrizeDistribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prize_distribution_id")
    private Long prizeDistributionId;

    @Column(name = "tournament_id", nullable = false)
    private Long tournamentId;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(nullable = false)
    private Integer placement;

    @Column(name = "prize_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal prizeAmount = BigDecimal.ZERO;

    @Column(name = "awarded_at")
    private LocalDate awardedAt;

    public PrizeDistribution() {}

    public Long getPrizeDistributionId() {
        return prizeDistributionId;
    }

    public void setPrizeDistributionId(Long prizeDistributionId) {
        this.prizeDistributionId = prizeDistributionId;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Integer getPlacement() {
        return placement;
    }

    public void setPlacement(Integer placement) {
        this.placement = placement;
    }

    public BigDecimal getPrizeAmount() {
        return prizeAmount;
    }

    public void setPrizeAmount(BigDecimal prizeAmount) {
        this.prizeAmount = prizeAmount;
    }

    public LocalDate getAwardedAt() {
        return awardedAt;
    }

    public void setAwardedAt(LocalDate awardedAt) {
        this.awardedAt = awardedAt;
    }
}