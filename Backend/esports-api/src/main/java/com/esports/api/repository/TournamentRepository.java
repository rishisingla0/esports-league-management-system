package com.esports.api.repository;

import com.esports.api.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository
        extends JpaRepository<Tournament, Long> {
}