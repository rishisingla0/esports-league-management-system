package com.esports.api.service;

import com.esports.api.dto.AddManagerRosterPlayerRequest;
import com.esports.api.dto.CreateTeamRosterRequest;
import com.esports.api.dto.ManagerMatchResponse;
import com.esports.api.dto.TeamRosterResponse;
import com.esports.api.dto.TournamentRegistrationResponse;
import com.esports.api.dto.CreateTournamentRegistrationRequest;
import com.esports.api.entity.Match;
import com.esports.api.entity.MatchTeam;
import com.esports.api.entity.TeamManagerProfile;
import com.esports.api.entity.TeamRoster;
import com.esports.api.entity.TournamentRegistration;
import com.esports.api.entity.User;
import com.esports.api.repository.MatchTeamRepository;
import com.esports.api.repository.TeamManagerProfileRepository;
import com.esports.api.repository.TeamRosterRepository;
import com.esports.api.repository.TournamentRegistrationRepository;
import com.esports.api.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class ManagerWorkspaceService {

    private final UserRepository userRepository;
    private final TeamManagerProfileRepository profileRepository;
    private final TeamRosterRepository rosterRepository;
    private final TeamRosterService teamRosterService;
    private final TournamentRegistrationRepository registrationRepository;
    private final TournamentRegistrationService registrationService;
    private final MatchTeamRepository matchTeamRepository;

    public ManagerWorkspaceService(
            UserRepository userRepository,
            TeamManagerProfileRepository profileRepository,
            TeamRosterRepository rosterRepository,
            TeamRosterService teamRosterService,
            TournamentRegistrationRepository registrationRepository,
            TournamentRegistrationService registrationService,
            MatchTeamRepository matchTeamRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.rosterRepository = rosterRepository;
        this.teamRosterService = teamRosterService;
        this.registrationRepository = registrationRepository;
        this.registrationService = registrationService;
        this.matchTeamRepository = matchTeamRepository;
    }

    public List<TeamRosterResponse> getMyRoster(String email) {
        Long teamId = getManagerProfile(email).getTeam().getTeamId();
        return rosterRepository.findByTeamIdAndStatusOrderByRosterIdAsc(teamId, "ACTIVE")
                .stream()
                .map(this::toRosterResponse)
                .toList();
    }

    @Transactional
    public TeamRosterResponse addPlayerToMyRoster(
            String email,
            AddManagerRosterPlayerRequest request) {
        Long teamId = getManagerProfile(email).getTeam().getTeamId();
        String role = request.role() == null || request.role().isBlank()
                ? "PLAYER"
                : request.role().trim();

        return teamRosterService.create(new CreateTeamRosterRequest(
                teamId,
                request.playerId(),
                role,
                request.startDate(),
                null,
                "ACTIVE"
        ));
    }

    @Transactional
    public void removePlayerFromMyRoster(String email, Long rosterId) {
        Long teamId = getManagerProfile(email).getTeam().getTeamId();
        TeamRosterResponse roster = teamRosterService.getById(rosterId);

        if (!teamId.equals(roster.teamId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Roster entry not found for your team");
        }

        // TeamRosterService preserves roster history by marking the entry inactive.
        teamRosterService.delete(rosterId);
    }

    public List<TournamentRegistrationResponse> getMyTournamentRegistrations(String email) {
        Long teamId = getManagerProfile(email).getTeam().getTeamId();
        return registrationRepository.findByTeamTeamIdOrderByRegistrationIdDesc(teamId)
                .stream()
                .map(this::toRegistrationResponse)
                .toList();
    }

    @Transactional
    public TournamentRegistrationResponse registerMyTeam(
            String email,
            Long tournamentId) {
        Long teamId = getManagerProfile(email).getTeam().getTeamId();
        return registrationService.createRegistration(
                new CreateTournamentRegistrationRequest(
                        tournamentId,
                        teamId,
                        LocalDate.now(),
                        null,
                        "REGISTERED"
                )
        );
    }

    public List<ManagerMatchResponse> getMyMatches(String email) {
        Long teamId = getManagerProfile(email).getTeam().getTeamId();
        return matchTeamRepository.findByTeamTeamIdOrderByMatchMatchIdDesc(teamId)
                .stream()
                .map(this::toManagerMatchResponse)
                .toList();
    }

    private TeamManagerProfile getManagerProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        if (!user.isEnabled() || !"TEAM_MANAGER".equals(user.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only enabled team managers can access this workspace");
        }

        return profileRepository.findByUserUserId(user.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.CONFLICT, "Complete team onboarding first"));
    }

    private TeamRosterResponse toRosterResponse(TeamRoster roster) {
        return new TeamRosterResponse(
                roster.getRosterId(),
                roster.getTeamId(),
                roster.getPlayerId(),
                roster.getRole(),
                roster.getStartDate(),
                roster.getEndDate(),
                roster.getStatus()
        );
    }

    private TournamentRegistrationResponse toRegistrationResponse(
            TournamentRegistration registration) {
        return new TournamentRegistrationResponse(
                registration.getRegistrationId(),
                registration.getTournament().getTournamentId(),
                registration.getTournament().getTournamentName(),
                registration.getTeam().getTeamId(),
                registration.getTeam().getTeamName(),
                registration.getRegistrationDate(),
                registration.getSeed(),
                registration.getStatus()
        );
    }

    private ManagerMatchResponse toManagerMatchResponse(MatchTeam matchTeam) {
        Match match = matchTeam.getMatch();
        return new ManagerMatchResponse(
                match.getMatchId(),
                match.getTournament().getTournamentId(),
                match.getTournament().getTournamentName(),
                match.getMatchNumber(),
                match.getMapName(),
                match.getScheduledAt(),
                match.getStatus(),
                matchTeam.getPlacement(),
                matchTeam.getTotalPoints()
        );
    }
}
