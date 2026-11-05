
package com.esports.api.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "match_squad_players",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_player_per_match",
            columnNames = {"match_id", "player_id"}
        ),
        @UniqueConstraint(
            name = "uq_player_per_match_team",
            columnNames = {"match_team_id", "player_id"}
        )
    }
)
public class MatchSquadPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_squad_player_id")
    private Long matchSquadPlayerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(
            name = "match_team_id",
            referencedColumnName = "match_team_id"
        ),
        @JoinColumn(
            name = "match_id",
            referencedColumnName = "match_id"
        )
    })
    private MatchTeam matchTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    public MatchSquadPlayer() {}

    public Long getMatchSquadPlayerId() {
        return matchSquadPlayerId;
    }

    public void setMatchSquadPlayerId(Long matchSquadPlayerId) {
        this.matchSquadPlayerId = matchSquadPlayerId;
    }

    public MatchTeam getMatchTeam() {
        return matchTeam;
    }

    public void setMatchTeam(MatchTeam matchTeam) {
        this.matchTeam = matchTeam;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
