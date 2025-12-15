package com.bankingsystem.simulator.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TransactionResponse {
    private String transactionId;
    private String type;
    private Double amount;
    private Date timestamp;
    private String status;
}
