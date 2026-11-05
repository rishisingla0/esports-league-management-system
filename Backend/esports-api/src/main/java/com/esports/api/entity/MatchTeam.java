package com.esports.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
    name = "match_teams",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_team_per_match",
            columnNames = {"match_id", "team_id"}
        )
    }
)
public class MatchTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_team_id")
    private Long matchTeamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "placement")
    private Integer placement;

    @Column(name = "total_points", precision = 8, scale = 2)
    private BigDecimal totalPoints;

    public MatchTeam() {
    }

    public Long getMatchTeamId() {
        return matchTeamId;
    }

    public void setMatchTeamId(Long matchTeamId) {
        this.matchTeamId = matchTeamId;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Integer getPlacement() {
        return placement;
    }

    public void setPlacement(Integer placement) {
        this.placement = placement;
    }

    public BigDecimal getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(BigDecimal totalPoints) {
        this.totalPoints = totalPoints;
    }
}