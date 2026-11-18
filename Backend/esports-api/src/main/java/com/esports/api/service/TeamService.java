
package com.esports.api.service;

import com.esports.api.dto.TeamResponse;
import com.esports.api.dto.UpdateTeamRequest;
import com.esports.api.dto.CreateTeamRequest;
import com.esports.api.entity.*;
import com.esports.api.repository.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityManager;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final OrganizationRepository organizationRepository;
    private final GameRepository gameRepository;
    private final CoachRepository coachRepository;
    private final UserRepository userRepository;
    private final TeamManagerProfileRepository profileRepository;
    private final EntityManager entityManager;

    public TeamService(
            TeamRepository teamRepository,
            OrganizationRepository organizationRepository,
            GameRepository gameRepository,
            CoachRepository coachRepository,
            UserRepository userRepository,
            TeamManagerProfileRepository profileRepository,
            EntityManager entityManager) {

        this.teamRepository = teamRepository;
        this.organizationRepository = organizationRepository;
        this.gameRepository = gameRepository;
        this.coachRepository = coachRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.entityManager = entityManager;
    }

    // GET ALL TEAMS - PUBLIC
    @Transactional(readOnly = true)
    public List<TeamResponse> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET TEAM BY ID - PUBLIC
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Team not found"));

        return convertToResponse(team);
    }

    // CREATE TEAM - TEAM MANAGER
    @Transactional
    public TeamResponse createTeam(
            String email,
            CreateTeamRequest request) {

        User user = getManager(email);

        if (profileRepository.existsByUserUserId(user.getUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "You already have a team assigned to your account");
        }

        Organization organization = organizationRepository
                .findById(request.organizationId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Organization not found"));

        Game game = gameRepository
                .findById(request.gameId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Game not found"));

        String teamName = request.teamName().trim();

        if (teamRepository
                .existsByOrganizationOrganizationIdAndGameGameIdAndTeamNameIgnoreCase(
                        request.organizationId(),
                        request.gameId(),
                        teamName)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Team already exists for this organization and game");
        }

        Team team = new Team();
        team.setOrganization(organization);
        team.setGame(game);
        team.setTeamName(teamName);

        if (request.coachId() != null) {
            Coach coach = coachRepository.findById(request.coachId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Coach not found"));

            team.setCoach(coach);
        }

        Team savedTeam = teamRepository.saveAndFlush(team);

        TeamManagerProfile profile = new TeamManagerProfile();
        profile.setUser(user);
        profile.setTeam(savedTeam);
        profileRepository.save(profile);

        entityManager.refresh(savedTeam);

        return convertToResponse(savedTeam);
    }

    // UPDATE OWN TEAM ONLY
    @Transactional
    public TeamResponse updateTeam(
            String email,
            Long id,
            UpdateTeamRequest request) {

        User user = getManager(email);
        Team team = getOwnedTeam(user.getUserId(), id);

        Organization organization = organizationRepository
                .findById(request.organizationId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Organization not found"));

        Game game = gameRepository
                .findById(request.gameId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Game not found"));

        String teamName = request.teamName().trim();

        if (teamRepository
                .existsByOrganizationOrganizationIdAndGameGameIdAndTeamNameIgnoreCaseAndTeamIdNot(
                        request.organizationId(),
                        request.gameId(),
                        teamName,
                        id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Team already exists for this organization and game");
        }

        team.setOrganization(organization);
        team.setGame(game);
        team.setTeamName(teamName);

        if (request.coachId() != null) {
            Coach coach = coachRepository.findById(request.coachId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Coach not found"));

            team.setCoach(coach);
        } else {
            team.setCoach(null);
        }

        return convertToResponse(teamRepository.save(team));
    }

    // DELETE OWN TEAM ONLY
    @Transactional
    public void deleteTeam(String email, Long id) {

        User user = getManager(email);
        Team team = getOwnedTeam(user.getUserId(), id);

        teamRepository.delete(team);
    }

    // VALIDATE AUTHENTICATED MANAGER
    private User getManager(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        if (!user.isEnabled() || !"TEAM_MANAGER".equals(user.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Access denied");
        }

        return user;
    }

    // VERIFY TEAM OWNERSHIP
    private Team getOwnedTeam(Long userId, Long teamId) {

        TeamManagerProfile profile = profileRepository
                .findByUserUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "You do not have an assigned team"));

        if (!profile.getTeam().getTeamId().equals(teamId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot modify another team's records");
        }

        return teamRepository.findById(teamId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Team not found"));
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
                        ? team.getCoach().getCoachId() : null,
                team.getCoach() != null
                        ? team.getCoach().getCoachName() : null,
                team.getCreatedAt()
        );
    }
}
