package com.esports.api.service;

import com.esports.api.dto.DashboardSummaryResponse;
import com.esports.api.repository.DashboardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardService(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getDashboardSummary() {

        return new DashboardSummaryResponse(
                dashboardRepository.countGames(),
                dashboardRepository.countTeams(),
                dashboardRepository.countPlayers(),
                dashboardRepository.countOrganizations(),
                dashboardRepository.countTournaments(),
                dashboardRepository.countOngoingMatches(),
                dashboardRepository.countScheduledMatches(),
                dashboardRepository.getTotalPrizeMoneyAwarded(),
                dashboardRepository.getActiveSponsorshipValue()
        );
    }
}