
package com.esports.api.service;

import com.esports.api.dto.CreateMatchSquadPlayerRequest;
import com.esports.api.dto.MatchSquadPlayerResponse;
import com.esports.api.entity.MatchSquadPlayer;
import com.esports.api.entity.MatchTeam;
import com.esports.api.entity.Player;
import com.esports.api.repository.MatchSquadPlayerRepository;
import com.esports.api.repository.MatchTeamRepository;
import com.esports.api.repository.PlayerRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class MatchSquadPlayerService {

    private final MatchSquadPlayerRepository squadRepository;
    private final MatchTeamRepository matchTeamRepository;
    private final PlayerRepository playerRepository;

    public MatchSquadPlayerService(
            MatchSquadPlayerRepository squadRepository,
            MatchTeamRepository matchTeamRepository,
            PlayerRepository playerRepository) {

        this.squadRepository = squadRepository;
        this.matchTeamRepository = matchTeamRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public List<MatchSquadPlayerResponse> getAllSquadPlayers() {
        return squadRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MatchSquadPlayerResponse getSquadPlayerById(Long id) {
        MatchSquadPlayer squadPlayer = squadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Match squad player not found with ID: " + id
                ));

        return mapToResponse(squadPlayer);
    }

    @Transactional(readOnly = true)
    public List<MatchSquadPlayerResponse> getPlayersByMatchTeamId(Long matchTeamId) {

        if (!matchTeamRepository.existsById(matchTeamId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Match team not found with ID: " + matchTeamId
            );
        }

        return squadRepository.findByMatchTeamMatchTeamId(matchTeamId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MatchSquadPlayerResponse> getPlayersByMatchId(Long matchId) {

        return squadRepository.findByMatchTeamMatchMatchId(matchId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MatchSquadPlayerResponse addSquadPlayer(
            CreateMatchSquadPlayerRequest request) {

        MatchTeam matchTeam = matchTeamRepository.findById(request.matchTeamId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Match team not found with ID: " + request.matchTeamId()
                ));

        Player player = playerRepository.findById(request.playerId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Player not found with ID: " + request.playerId()
                ));

        Long matchId = matchTeam.getMatch().getMatchId();

        if (squadRepository.existsByMatchTeamMatchMatchIdAndPlayerPlayerId(
                matchId, player.getPlayerId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Player is already assigned to this match"
            );
        }

        MatchSquadPlayer squadPlayer = new MatchSquadPlayer();
squadPlayer.setMatchTeam(matchTeam);
squadPlayer.setMatchId(matchTeam.getMatch().getMatchId());
squadPlayer.setPlayer(player);

        try {
            MatchSquadPlayer saved = squadRepository.saveAndFlush(squadPlayer);
            return mapToResponse(saved);

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot add player. Check roster eligibility, game profile, match status, or duplicate assignment.",
                    ex
            );
        }
    }

    public void removeSquadPlayer(Long id) {

        MatchSquadPlayer squadPlayer = squadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Match squad player not found with ID: " + id
                ));

        try {
            squadRepository.delete(squadPlayer);
            squadRepository.flush();

        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot remove this player. The match may no longer be scheduled.",
                    ex
            );
        }
    }

    private MatchSquadPlayerResponse mapToResponse(
            MatchSquadPlayer squadPlayer) {

        MatchTeam matchTeam = squadPlayer.getMatchTeam();
        Player player = squadPlayer.getPlayer();

        return new MatchSquadPlayerResponse(
                squadPlayer.getMatchSquadPlayerId(),
                squadPlayer.getMatchId(),
                matchTeam.getMatchTeamId(),
                matchTeam.getTeam().getTeamId(),
                matchTeam.getTeam().getTeamName(),
                player.getPlayerId(),
                player.getPlayerName()
        );
    }
}
