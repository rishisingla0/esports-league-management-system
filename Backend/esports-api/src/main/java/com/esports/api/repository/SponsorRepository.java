package com.esports.api.repository;

import com.esports.api.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    boolean existsBySponsorName(String sponsorName);
}