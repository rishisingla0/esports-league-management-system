package com.esports.api.repository;

import com.esports.api.entity.MatchTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchTeamRepository extends JpaRepository<MatchTeam, Long> {

    List<MatchTeam> findByMatchMatchId(Long matchId);

    boolean existsByMatchMatchIdAndTeamTeamId(
            Long matchId,
            Long teamId
    );

    boolean existsByMatchMatchIdAndTeamTeamIdAndMatchTeamIdNot(
            Long matchId,
            Long teamId,
            Long matchTeamId
    );
    
boolean existsByMatchMatchIdAndPlacementIsNull(Long matchId);

boolean existsByMatchMatchIdAndTotalPointsIsNull(Long matchId);



}