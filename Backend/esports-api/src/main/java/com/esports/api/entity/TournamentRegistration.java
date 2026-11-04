package com.esports.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
    name = "tournament_registrations",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_tournament_team",
            columnNames = {"tournament_id", "team_id"}
        )
    }
)
public class TournamentRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private Long registrationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "seed")
    private Integer seed;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    public TournamentRegistration() {}

    public Long getRegistrationId() { return registrationId; }
    public Tournament getTournament() { return tournament; }
    public Team getTeam() { return team; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public Integer getSeed() { return seed; }
    public String getStatus() { return status; }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}