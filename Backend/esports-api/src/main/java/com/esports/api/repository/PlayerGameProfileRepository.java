package com.esports.api.repository;

import com.esports.api.entity.PlayerGameProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerGameProfileRepository
        extends JpaRepository<PlayerGameProfile, Long> {

    boolean existsByPlayerPlayerIdAndGameGameId(
            Long playerId, Long gameId);

    boolean existsByGameGameIdAndInGameIdIgnoreCase(
            Long gameId, String inGameId);

    boolean existsByPlayerPlayerIdAndGameGameIdAndProfileIdNot(
            Long playerId, Long gameId, Long profileId);

    boolean existsByGameGameIdAndInGameIdIgnoreCaseAndProfileIdNot(
            Long gameId, String inGameId, Long profileId);
}