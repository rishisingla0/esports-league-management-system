package com.esports.api.controller;

import com.esports.api.dto.AddManagerRosterPlayerRequest;
import com.esports.api.dto.ManagerMatchResponse;
import com.esports.api.dto.TeamRosterResponse;
import com.esports.api.dto.TournamentRegistrationResponse;
import com.esports.api.service.ManagerWorkspaceService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager/workspace")
public class ManagerWorkspaceController {

    private final ManagerWorkspaceService workspaceService;

    public ManagerWorkspaceController(ManagerWorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping("/roster")
    public List<TeamRosterResponse> getMyRoster(Authentication authentication) {
        return workspaceService.getMyRoster(authentication.getName());
    }

    @PostMapping("/roster")
    @ResponseStatus(HttpStatus.CREATED)
    public TeamRosterResponse addPlayer(
            Authentication authentication,
            @Valid @RequestBody AddManagerRosterPlayerRequest request) {
        return workspaceService.addPlayerToMyRoster(authentication.getName(), request);
    }

    @DeleteMapping("/roster/{rosterId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePlayer(
            Authentication authentication,
            @PathVariable Long rosterId) {
        workspaceService.removePlayerFromMyRoster(authentication.getName(), rosterId);
    }

    @GetMapping("/tournament-registrations")
    public List<TournamentRegistrationResponse> getMyTournamentRegistrations(
            Authentication authentication) {
        return workspaceService.getMyTournamentRegistrations(authentication.getName());
    }

    @PostMapping("/tournaments/{tournamentId}/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TournamentRegistrationResponse registerMyTeam(
            Authentication authentication,
            @PathVariable Long tournamentId) {
        return workspaceService.registerMyTeam(authentication.getName(), tournamentId);
    }

    @GetMapping("/matches")
    public List<ManagerMatchResponse> getMyMatches(Authentication authentication) {
        return workspaceService.getMyMatches(authentication.getName());
    }
}
