package com.esports.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "matches",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_tournament_match_number",
            columnNames = {"tournament_id", "match_number"}
        )
    }
)
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long matchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @Column(name = "match_number", nullable = false)
    private Integer matchNumber;

    @Column(name = "map_name", nullable = false, length = 100)
    private String mapName;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    public Match() {}

    public Long getMatchId() { return matchId; }
    public Tournament getTournament() { return tournament; }
    public Integer getMatchNumber() { return matchNumber; }
    public String getMapName() { return mapName; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public String getStatus() { return status; }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public void setMatchNumber(Integer matchNumber) {
        this.matchNumber = matchNumber;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}