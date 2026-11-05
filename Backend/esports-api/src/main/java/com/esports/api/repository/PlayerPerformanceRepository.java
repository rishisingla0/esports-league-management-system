package com.esports.api.repository;

import com.esports.api.entity.PlayerPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlayerPerformanceRepository
        extends JpaRepository<PlayerPerformance, Long> {

    List<PlayerPerformance> findByMatchSquadPlayerMatchId(Long matchId);

    List<PlayerPerformance> findByMatchSquadPlayerMatchTeamMatchTeamId(Long matchTeamId);

    List<PlayerPerformance> findByMatchSquadPlayerPlayerPlayerId(Long playerId);

    boolean existsByMatchSquadPlayerMatchSquadPlayerId(Long matchSquadPlayerId);

    boolean existsByMatchSquadPlayerMatchSquadPlayerIdAndPerformanceIdNot(
            Long matchSquadPlayerId, Long performanceId
    );
}