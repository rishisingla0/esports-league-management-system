package com.esports.api.service;

import com.esports.api.dto.*;
import com.esports.api.entity.PlayerTransfer;
import com.esports.api.repository.PlayerTransferRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PlayerTransferService {

    private final PlayerTransferRepository repository;

    public PlayerTransferService(PlayerTransferRepository repository) {
        this.repository = repository;
    }

    public List<PlayerTransferResponse> getAll() {
        return repository.findAll().stream().map(this::mapToResponse).toList();
    }

    public PlayerTransferResponse getById(Long id) {
        return mapToResponse(findTransfer(id));
    }

    public List<PlayerTransferResponse> getByPlayer(Long playerId) {
        return repository.findByPlayerIdOrderByTransferDateDesc(playerId)
                .stream().map(this::mapToResponse).toList();
    }

    public List<PlayerTransferResponse> getByTeam(Long teamId) {
        return repository.findByFromTeamIdOrToTeamIdOrderByTransferDateDesc(
                teamId, teamId
        ).stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public PlayerTransferResponse create(CreatePlayerTransferRequest request) {
        validate(request.fromTeamId(), request.toTeamId(),
                request.transferFee(), request.contractMonths());

        PlayerTransfer transfer = new PlayerTransfer();
        applyRequest(transfer, request.playerId(), request.fromTeamId(),
                request.toTeamId(), request.transferDate(),
                request.transferFee(), request.contractMonths());

        try {
            return mapToResponse(repository.saveAndFlush(transfer));
        } catch (DataAccessException ex) {
            throw conflict();
        }
    }

    @Transactional
    public PlayerTransferResponse update(Long id, UpdatePlayerTransferRequest request) {
        validate(request.fromTeamId(), request.toTeamId(),
                request.transferFee(), request.contractMonths());

        PlayerTransfer transfer = findTransfer(id);

        applyRequest(transfer, request.playerId(), request.fromTeamId(),
                request.toTeamId(), request.transferDate(),
                request.transferFee(), request.contractMonths());

        try {
            return mapToResponse(repository.saveAndFlush(transfer));
        } catch (DataAccessException ex) {
            throw conflict();
        }
    }

    @Transactional
    public void delete(Long id) {
        PlayerTransfer transfer = findTransfer(id);

        try {
            repository.delete(transfer);
            repository.flush();
        } catch (DataAccessException ex) {
            throw conflict();
        }
    }

    private PlayerTransfer findTransfer(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Player transfer not found"));
    }

    private void validate(Long fromTeamId, Long toTeamId,
                          BigDecimal fee, Integer contractMonths) {

        if (fromTeamId.equals(toTeamId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Source and destination teams must be different");
        }

        if (fee.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Transfer fee cannot be negative");
        }

        if (contractMonths != null && contractMonths <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Contract months must be positive");
        }
    }

    private void applyRequest(PlayerTransfer transfer, Long playerId,
                              Long fromTeamId, Long toTeamId,
                              java.time.LocalDate transferDate,
                              BigDecimal transferFee, Integer contractMonths) {

        transfer.setPlayerId(playerId);
        transfer.setFromTeamId(fromTeamId);
        transfer.setToTeamId(toTeamId);
        transfer.setTransferDate(transferDate);
        transfer.setTransferFee(transferFee);
        transfer.setContractMonths(contractMonths);
    }

    private PlayerTransferResponse mapToResponse(PlayerTransfer transfer) {
        return new PlayerTransferResponse(
                transfer.getTransferId(),
                transfer.getPlayerId(),
                transfer.getFromTeamId(),
                transfer.getToTeamId(),
                transfer.getTransferDate(),
                transfer.getTransferFee(),
                transfer.getContractMonths()
        );
    }

    private ResponseStatusException conflict() {
        return new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Transfer operation violates a database constraint");
    }
}