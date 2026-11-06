
package com.esports.api.service;

import com.esports.api.dto.*;
import com.esports.api.entity.Coach;
import com.esports.api.repository.CoachRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CoachService {

    private final CoachRepository coachRepository;

    public CoachService(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    public List<CoachResponse> getAllCoaches() {
        return coachRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CoachResponse getCoachById(Long id) {
        Coach coach = findCoach(id);
        return mapToResponse(coach);
    }

    @Transactional
    public CoachResponse createCoach(CreateCoachRequest request) {
        Coach coach = new Coach();
        coach.setCoachName(request.coachName());
        coach.setCountry(request.country());
        coach.setHireDate(request.hireDate());

        return mapToResponse(coachRepository.save(coach));
    }

    @Transactional
    public CoachResponse updateCoach(Long id, UpdateCoachRequest request) {
        Coach coach = findCoach(id);

        coach.setCoachName(request.coachName());
        coach.setCountry(request.country());
        coach.setHireDate(request.hireDate());

        return mapToResponse(coachRepository.save(coach));
    }

    @Transactional
    public void deleteCoach(Long id) {
        Coach coach = findCoach(id);
        coachRepository.delete(coach);
    }

    private Coach findCoach(Long id) {
        return coachRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Coach not found"));
    }

    private CoachResponse mapToResponse(Coach coach) {
        return new CoachResponse(
                coach.getCoachId(),
                coach.getCoachName(),
                coach.getCountry(),
                coach.getHireDate()
        );
    }
}
