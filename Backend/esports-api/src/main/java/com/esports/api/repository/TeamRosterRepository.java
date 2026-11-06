package com.esports.api.repository;

import com.esports.api.entity.TeamRoster;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamRosterRepository extends JpaRepository<TeamRoster, Long> {

    List<TeamRoster> findByTeamIdOrderByRosterIdAsc(Long teamId);

    List<TeamRoster> findByPlayerIdOrderByRosterIdAsc(Long playerId);
}