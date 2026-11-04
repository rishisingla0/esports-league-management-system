package com.esports.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "player_game_profiles")
public class PlayerGameProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "in_game_id", nullable = false, length = 100)
    private String inGameId;

    public PlayerGameProfile() {}

    public Long getProfileId() {
        return profileId;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public String getInGameId() {
        return inGameId;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setInGameId(String inGameId) {
        this.inGameId = inGameId;
    }
}