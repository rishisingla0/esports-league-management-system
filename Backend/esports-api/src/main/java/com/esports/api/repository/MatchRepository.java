package com.esports.api.repository;

import com.esports.api.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {

    boolean existsByTournamentTournamentIdAndMatchNumber(
            Long tournamentId,
            Integer matchNumber
    );

    boolean existsByTournamentTournamentIdAndMatchNumberAndMatchIdNot(
            Long tournamentId,
            Integer matchNumber,
            Long matchId
    );
}