
package com.esports.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "game_name", nullable = false, unique = true, length = 100)
    private String gameName;

    @Column(name = "genre", nullable = false, length = 50)
    private String genre;

    @Column(name = "developer", nullable = false, length = 100)
    private String developer;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    public Game() {
    }

    public Long getGameId() {
        return gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGenre() {
        return genre;
    }

    public String getDeveloper() {
        return developer;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }
}
