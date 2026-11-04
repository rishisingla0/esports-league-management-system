package com.esports.api.repository;

import com.esports.api.entity.TournamentRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRegistrationRepository
        extends JpaRepository<TournamentRegistration, Long> {

    boolean existsByTournamentTournamentIdAndTeamTeamId(
        Long tournamentId,
        Long teamId
    );

    boolean existsByTournamentTournamentIdAndTeamTeamIdAndRegistrationIdNot(
        Long tournamentId,
        Long teamId,
        Long registrationId
    );
}