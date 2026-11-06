package com.esports.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "team_rosters")
public class TeamRoster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roster_id")
    private Long rosterId;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(length = 50)
    private String role;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";

    public TeamRoster() {}

    public Long getRosterId() { return rosterId; }
    public void setRosterId(Long rosterId) { this.rosterId = rosterId; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}