package com.esports.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "player_transfers")
public class PlayerTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private Long transferId;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "from_team_id", nullable = false)
    private Long fromTeamId;

    @Column(name = "to_team_id", nullable = false)
    private Long toTeamId;

    @Column(name = "transfer_date", nullable = false)
    private LocalDate transferDate;

    @Column(name = "transfer_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal transferFee = BigDecimal.ZERO;

    @Column(name = "contract_months")
    private Integer contractMonths;

    public PlayerTransfer() {}

    public Long getTransferId() { return transferId; }
    public void setTransferId(Long transferId) { this.transferId = transferId; }

    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public Long getFromTeamId() { return fromTeamId; }
    public void setFromTeamId(Long fromTeamId) { this.fromTeamId = fromTeamId; }

    public Long getToTeamId() { return toTeamId; }
    public void setToTeamId(Long toTeamId) { this.toTeamId = toTeamId; }

    public LocalDate getTransferDate() { return transferDate; }
    public void setTransferDate(LocalDate transferDate) { this.transferDate = transferDate; }

    public BigDecimal getTransferFee() { return transferFee; }
    public void setTransferFee(BigDecimal transferFee) { this.transferFee = transferFee; }

    public Integer getContractMonths() { return contractMonths; }
    public void setContractMonths(Integer contractMonths) { this.contractMonths = contractMonths; }
}