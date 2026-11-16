
package com.esports.api.service;

import com.esports.api.dto.CreateOrganizationAndTeamRequest;
import com.esports.api.dto.TeamOnboardingRequest;
import com.esports.api.dto.TeamOnboardingResponse;
import com.esports.api.dto.ManagerProfileResponse;

import com.esports.api.entity.*;
import com.esports.api.repository.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TeamManagerOnboardingService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final TeamManagerProfileRepository profileRepository;

    public TeamManagerOnboardingService(
            UserRepository userRepository,
            OrganizationRepository organizationRepository,
            GameRepository gameRepository,
            TeamRepository teamRepository,
            TeamManagerProfileRepository profileRepository) {

        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.gameRepository = gameRepository;
        this.teamRepository = teamRepository;
        this.profileRepository = profileRepository;
    }

    // CREATE NEW ORGANIZATION + TEAM
    @Transactional
    public TeamOnboardingResponse createOrganizationAndTeam(
            String email,
            CreateOrganizationAndTeamRequest request) {

        User user = getManager(email);
        checkManagerHasNoTeam(user.getUserId());

        String organizationName = request.organizationName().trim();

        if (organizationRepository.existsByOrganizationNameIgnoreCase(
                organizationName)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Organization already exists. Select the existing organization instead.");
        }

        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Game not found"));

        Organization organization = new Organization();
        organization.setOrganizationName(organizationName);
        organization.setCountry(request.country());
        organization.setFoundedDate(request.foundedDate());
        organization.setWebsite(request.website());

        Organization savedOrganization =
                organizationRepository.saveAndFlush(organization);

        Team team = createTeam(
                savedOrganization,
                game,
                request.teamName());

        return linkManagerToTeam(
                user,
                team,
                "Organization and team created successfully");
    }

    // CREATE TEAM IN EXISTING ORGANIZATION
    @Transactional
    public TeamOnboardingResponse createTeamInExistingOrganization(
            String email,
            TeamOnboardingRequest request) {

        User user = getManager(email);
        checkManagerHasNoTeam(user.getUserId());

        Organization organization =
                organizationRepository.findById(request.organizationId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Organization not found"));

        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Game not found"));

        Team team = createTeam(
                organization,
                game,
                request.teamName());

        return linkManagerToTeam(
                user,
                team,
                "Team created and linked to your account successfully");
    }

    // FIND AND VALIDATE MANAGER
    private User getManager(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        if (!user.isEnabled() || !"TEAM_MANAGER".equals(user.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only enabled team managers can complete onboarding");
        }

        return user;
    }

    // CHECK ONE-TEAM RESTRICTION
    private void checkManagerHasNoTeam(Long userId) {

        if (profileRepository.existsById(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "You already have a team assigned to your account");
        }
    }

    // CREATE TEAM
    private Team createTeam(
            Organization organization,
            Game game,
            String requestedTeamName) {

        String teamName = requestedTeamName.trim();

        if (teamRepository
                .existsByOrganizationOrganizationIdAndGameGameIdAndTeamNameIgnoreCase(
                        organization.getOrganizationId(),
                        game.getGameId(),
                        teamName)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A team with this name already exists for this organization and game");
        }

        Team team = new Team();
        team.setOrganization(organization);
        team.setGame(game);
        team.setTeamName(teamName);

        return teamRepository.saveAndFlush(team);
    }

    // LINK MANAGER TO TEAM
    private TeamOnboardingResponse linkManagerToTeam(
            User user,
            Team team,
            String message) {

        TeamManagerProfile profile = new TeamManagerProfile();
        profile.setUser(user);
        profile.setTeam(team);

        profileRepository.save(profile);

        return new TeamOnboardingResponse(
                user.getUserId(),
                team.getOrganization().getOrganizationId(),
                team.getOrganization().getOrganizationName(),
                team.getTeamId(),
                team.getTeamName(),
                team.getGame().getGameId(),
                team.getGame().getGameName(),
                message
        );
    }

    // GET LOGGED-IN MANAGER PROFILE
    @Transactional(readOnly = true)
    public ManagerProfileResponse getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        if (!user.isEnabled() || !"TEAM_MANAGER".equals(user.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Access denied");
        }

        return profileRepository.findByUserUserId(user.getUserId())
                .map(profile -> {

                    Team team = profile.getTeam();
                    Organization organization = team.getOrganization();
                    Game game = team.getGame();

                    return new ManagerProfileResponse(
                            user.getUserId(),
                            user.getFullName(),
                            user.getEmail(),
                            user.getRole(),
                            true,
                            organization.getOrganizationId(),
                            organization.getOrganizationName(),
                            team.getTeamId(),
                            team.getTeamName(),
                            game.getGameId(),
                            game.getGameName()
                    );
                })
                .orElseGet(() -> new ManagerProfileResponse(
                        user.getUserId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole(),
                        false,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ));
    }
}
