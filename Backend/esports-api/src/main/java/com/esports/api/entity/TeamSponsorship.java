package com.esports.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "team_sponsorships")
public class TeamSponsorship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sponsorship_id")
    private Long sponsorshipId;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "sponsor_id", nullable = false)
    private Long sponsorId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "sponsorship_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal sponsorshipAmount = BigDecimal.ZERO;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";

    public TeamSponsorship() {}

    public Long getSponsorshipId() { return sponsorshipId; }
    public void setSponsorshipId(Long sponsorshipId) { this.sponsorshipId = sponsorshipId; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public Long getSponsorId() { return sponsorId; }
    public void setSponsorId(Long sponsorId) { this.sponsorId = sponsorId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public BigDecimal getSponsorshipAmount() { return sponsorshipAmount; }
    public void setSponsorshipAmount(BigDecimal sponsorshipAmount) { this.sponsorshipAmount = sponsorshipAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}