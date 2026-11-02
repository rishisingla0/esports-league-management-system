
package com.esports.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "teams",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_team_name_per_game_org",
            columnNames = {"organization_id", "game_id", "team_name"}
        )
    }
)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id")
    private Coach coach;

    @Column(name = "team_name", nullable = false, length = 100)
    private String teamName;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Team() {
    }

    public Long getTeamId() {
        return teamId;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Game getGame() {
        return game;
    }

    public Coach getCoach() {
        return coach;
    }

    public String getTeamName() {
        return teamName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
