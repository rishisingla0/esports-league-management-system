
package com.esports.api.repository;

import com.esports.api.entity.TeamManagerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamManagerProfileRepository
        extends JpaRepository<TeamManagerProfile, Long> {

    Optional<TeamManagerProfile> findByUserUserId(Long userId);

    boolean existsByUserUserId(Long userId);

    boolean existsByTeamTeamId(Long teamId);
}
