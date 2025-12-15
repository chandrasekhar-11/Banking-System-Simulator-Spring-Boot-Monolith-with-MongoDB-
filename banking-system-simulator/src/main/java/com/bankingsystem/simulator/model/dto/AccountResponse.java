package com.bankingsystem.simulator.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponse {
    private String accountNumber;
    private String holderName;
    private Double balance;
    private String status;
}
