
package com.esports.api.repository;

import com.esports.api.entity.MatchSquadPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchSquadPlayerRepository
        extends JpaRepository<MatchSquadPlayer, Long> {

    List<MatchSquadPlayer> findByMatchTeamMatchTeamId(Long matchTeamId);

    List<MatchSquadPlayer> findByMatchTeamMatchMatchId(Long matchId);

    boolean existsByMatchTeamMatchMatchIdAndPlayerPlayerId(
        Long matchId,
        Long playerId
    );
}

