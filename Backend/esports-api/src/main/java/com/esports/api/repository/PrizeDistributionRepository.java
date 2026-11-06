package com.esports.api.repository;

import com.esports.api.entity.PrizeDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrizeDistributionRepository
        extends JpaRepository<PrizeDistribution, Long> {

    List<PrizeDistribution> findByTournamentIdOrderByPlacementAsc(Long tournamentId);

    List<PrizeDistribution> findByTeamIdOrderByPrizeDistributionIdAsc(Long teamId);
}