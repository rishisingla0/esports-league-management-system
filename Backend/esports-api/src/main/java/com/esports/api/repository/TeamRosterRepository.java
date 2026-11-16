
package com.esports.api.repository;

import com.esports.api.entity.TeamRoster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRosterRepository extends JpaRepository<TeamRoster, Long> {

    List<TeamRoster> findByTeamIdOrderByRosterIdAsc(Long teamId);

    List<TeamRoster> findByPlayerIdOrderByRosterIdAsc(Long playerId);

    List<TeamRoster> findByTeamIdAndStatusOrderByRosterIdAsc(
            Long teamId, String status);

    Optional<TeamRoster> findByTeamIdAndPlayerIdAndStatus(
            Long teamId, Long playerId, String status);

    boolean existsByTeamIdAndPlayerIdAndStatus(
            Long teamId, Long playerId, String status);
}
