package com.esports.api.repository;

import com.esports.api.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByOrganizationOrganizationIdAndGameGameIdAndTeamNameIgnoreCase(
            Long organizationId,
            Long gameId,
            String teamName
    );
    boolean existsByOrganizationOrganizationIdAndGameGameIdAndTeamNameIgnoreCaseAndTeamIdNot(
        Long organizationId,
        Long gameId,
        String teamName,
        Long teamId
);

}