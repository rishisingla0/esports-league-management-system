package com.esports.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "player_performances")
public class PlayerPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_id")
    private Long performanceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_squad_player_id" , nullable = false, unique = true)
    private MatchSquadPlayer matchSquadPlayer;

    @Column(name = "kills", nullable = false)
    private Integer kills = 0;

    @Column(name = "damage", nullable = false)
    private Integer damage = 0;

    @Column(name = "survival_time_seconds")
    private Integer survivalTimeSeconds;

    @Column(name = "assists", nullable = false)
    private Integer assists = 0;

    public PlayerPerformance() {}

    public Long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
    }

    public MatchSquadPlayer getMatchSquadPlayer() {
        return matchSquadPlayer;
    }

    public void setMatchSquadPlayer(MatchSquadPlayer matchSquadPlayer) {
        this.matchSquadPlayer = matchSquadPlayer;
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public Integer getSurvivalTimeSeconds() {
        return survivalTimeSeconds;
    }

    public void setSurvivalTimeSeconds(Integer survivalTimeSeconds) {
        this.survivalTimeSeconds = survivalTimeSeconds;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }
}