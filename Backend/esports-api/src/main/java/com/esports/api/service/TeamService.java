
package com.esports.api.service;

import com.esports.api.dto.CreateTeamRequest;
import com.esports.api.entity.*;
import com.esports.api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final OrganizationRepository organizationRepository;
    private final GameRepository gameRepository;
    private final CoachRepository coachRepository;

    public TeamService(
            TeamRepository teamRepository,
            OrganizationRepository organizationRepository,
            GameRepository gameRepository,
            CoachRepository coachRepository) {

        this.teamRepository = teamRepository;
        this.organizationRepository = organizationRepository;
        this.gameRepository = gameRepository;
        this.coachRepository = coachRepository;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team not found with ID: " + id));
    }

    @Transactional
    public Team createTeam(CreateTeamRequest request) {

        Organization organization = organizationRepository
                .findById(request.organizationId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Organization not found"));

        Game game = gameRepository
                .findById(request.gameId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Game not found"));

        if (teamRepository
                .existsByOrganizationOrganizationIdAndGameGameIdAndTeamNameIgnoreCase(
                        request.organizationId(),
                        request.gameId(),
                        request.teamName().trim())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Team already exists for this organization and game");
        }

        Team team = new Team();

        team.setOrganization(organization);
        team.setGame(game);
        team.setTeamName(request.teamName().trim());

        if (request.coachId() != null) {
            Coach coach = coachRepository
                    .findById(request.coachId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Coach not found"));

            team.setCoach(coach);
        }

        return teamRepository.save(team);
    }
}
