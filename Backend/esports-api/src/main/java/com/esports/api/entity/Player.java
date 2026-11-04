package com.esports.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "player_name", nullable = false, length = 100)
    private String playerName;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    public Player() {
    }

    public Long getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getCountry() {
        return country;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}