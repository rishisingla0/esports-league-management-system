package com.esports.api.repository;

import com.esports.api.entity.PlayerTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlayerTransferRepository extends JpaRepository<PlayerTransfer, Long> {

    List<PlayerTransfer> findByPlayerIdOrderByTransferDateDesc(Long playerId);

    List<PlayerTransfer> findByFromTeamIdOrToTeamIdOrderByTransferDateDesc(
        Long fromTeamId, Long toTeamId
    );
}