package com.esports.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tournament_id")
    private Long tournamentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "tournament_name", nullable = false, length = 150)
    private String tournamentName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "prize_pool", nullable = false, precision = 14, scale = 2)
    private BigDecimal prizePool;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    public Tournament() {}

    public Long getTournamentId() { return tournamentId; }
    public Game getGame() { return game; }
    public String getTournamentName() { return tournamentName; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public BigDecimal getPrizePool() { return prizePool; }
    public String getStatus() { return status; }

    public void setGame(Game game) { this.game = game; }
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public void setPrizePool(BigDecimal prizePool) {
        this.prizePool = prizePool;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}