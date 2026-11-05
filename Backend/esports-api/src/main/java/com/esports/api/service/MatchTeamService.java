package com.esports.api.service;

import com.esports.api.dto.CreateMatchTeamRequest;
import com.esports.api.dto.MatchTeamResponse;
import com.esports.api.dto.UpdateMatchTeamRequest;
import com.esports.api.entity.Match;
import com.esports.api.entity.MatchTeam;
import com.esports.api.entity.Team;
import com.esports.api.repository.MatchRepository;
import com.esports.api.repository.MatchTeamRepository;
import com.esports.api.repository.TeamRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class MatchTeamService {

    private final MatchTeamRepository matchTeamRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    public MatchTeamService(
            MatchTeamRepository matchTeamRepository,
            MatchRepository matchRepository,
            TeamRepository teamRepository
    ) {
        this.matchTeamRepository = matchTeamRepository;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    // GET ALL MATCH TEAMS
    @Transactional(readOnly = true)
    public List<MatchTeamResponse> getAllMatchTeams() {

        return matchTeamRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET MATCH TEAM BY ID
    @Transactional(readOnly = true)
    public MatchTeamResponse getMatchTeamById(Long id) {

        MatchTeam matchTeam = matchTeamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Match team not found with ID: " + id
                ));

        return mapToResponse(matchTeam);
    }

    // GET ALL TEAMS IN A PARTICULAR MATCH
    @Transactional(readOnly = true)
    public List<MatchTeamResponse> getTeamsByMatchId(Long matchId) {

        if (!matchRepository.existsById(matchId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Match not found with ID: " + matchId
            );
        }

        return matchTeamRepository.findByMatchMatchId(matchId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // CREATE MATCH TEAM
    public MatchTeamResponse createMatchTeam(
            CreateMatchTeamRequest request
    ) {

        Match match = matchRepository.findById(request.matchId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Match not found with ID: " + request.matchId()
                ));

        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team not found with ID: " + request.teamId()
                ));

        if (matchTeamRepository.existsByMatchMatchIdAndTeamTeamId(
                request.matchId(),
                request.teamId()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This team is already participating in this match"
            );
        }

        MatchTeam matchTeam = new MatchTeam();

        matchTeam.setMatch(match);
        matchTeam.setTeam(team);

        try {
            MatchTeam savedMatchTeam =
                    matchTeamRepository.saveAndFlush(matchTeam);

            return mapToResponse(savedMatchTeam);

        } catch (DataIntegrityViolationException ex) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot add team. Check tournament registration, game eligibility, and match status."
            );
        }
    }

    // UPDATE MATCH TEAM
    public MatchTeamResponse updateMatchTeam(
            Long id,
            UpdateMatchTeamRequest request
    ) {

        MatchTeam matchTeam = matchTeamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Match team not found with ID: " + id
                ));

        matchTeam.setPlacement(request.placement());
        matchTeam.setTotalPoints(request.totalPoints());

        try {
            MatchTeam updatedMatchTeam =
                    matchTeamRepository.saveAndFlush(matchTeam);

            return mapToResponse(updatedMatchTeam);

        } catch (DataIntegrityViolationException ex) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot update match team. Check match status and database constraints."
            );
        }
    }

    // DELETE MATCH TEAM
    public void deleteMatchTeam(Long id) {

        MatchTeam matchTeam = matchTeamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Match team not found with ID: " + id
                ));

        try {
            matchTeamRepository.delete(matchTeam);
            matchTeamRepository.flush();

        } catch (DataIntegrityViolationException ex) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot remove this team. Check match status and squad dependencies."
            );
        }
    }

    // ENTITY TO RESPONSE DTO
    private MatchTeamResponse mapToResponse(MatchTeam matchTeam) {

        return new MatchTeamResponse(
                matchTeam.getMatchTeamId(),
                matchTeam.getMatch().getMatchId(),
                matchTeam.getMatch().getMatchNumber(),
                matchTeam.getTeam().getTeamId(),
                matchTeam.getTeam().getTeamName(),
                matchTeam.getPlacement(),
                matchTeam.getTotalPoints()
        );
    }
}