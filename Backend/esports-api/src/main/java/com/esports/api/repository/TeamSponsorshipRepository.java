package com.esports.api.repository;

import com.esports.api.entity.TeamSponsorship;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamSponsorshipRepository extends JpaRepository<TeamSponsorship, Long> {

    List<TeamSponsorship> findByTeamIdOrderBySponsorshipIdAsc(Long teamId);

    List<TeamSponsorship> findBySponsorIdOrderBySponsorshipIdAsc(Long sponsorId);
}