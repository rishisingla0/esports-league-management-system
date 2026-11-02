
package com.esports.api.service;

import com.esports.api.dto.TeamResponse;
import com.esports.api.dto.CreateTeamRequest;
import com.esports.api.entity.*;
import com.esports.api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import jakarta.persistence.EntityManager;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final OrganizationRepository organizationRepository;
    private final GameRepository gameRepository;
    private final CoachRepository coachRepository;
    private final EntityManager entityManager;

    public TeamService(
            TeamRepository teamRepository,
            OrganizationRepository organizationRepository,
            GameRepository gameRepository,
            CoachRepository coachRepository,
            EntityManager entityManager) {

        this.teamRepository = teamRepository;
        this.organizationRepository = organizationRepository;
        this.gameRepository = gameRepository;
        this.coachRepository = coachRepository;
        this.entityManager = entityManager;
    }
@Transactional(readOnly = true)
public List<TeamResponse> getAllTeams() {
    return teamRepository.findAll()
            .stream()
            .map(this::convertToResponse)
            .toList();
}

@Transactional(readOnly = true)
public TeamResponse getTeamById(Long id) {
    Team team = teamRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Team not found with ID: " + id));

    return convertToResponse(team);
}

    @Transactional
    public TeamResponse createTeam(CreateTeamRequest request) {

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

Team savedTeam = teamRepository.saveAndFlush(team);
entityManager.refresh(savedTeam);

        return convertToResponse(savedTeam);
    }
    private TeamResponse convertToResponse(Team team) {
    return new TeamResponse(
            team.getTeamId(),
            team.getTeamName(),

            team.getOrganization().getOrganizationId(),
            team.getOrganization().getOrganizationName(),

            team.getGame().getGameId(),
            team.getGame().getGameName(),

            team.getCoach() != null
                    ? team.getCoach().getCoachId()
                    : null,

            team.getCoach() != null
                    ? team.getCoach().getCoachName()
                    : null,

            team.getCreatedAt()
    );
}
}
