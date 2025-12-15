package com.bankingsystem.simulator.model.dto;

import lombok.Data;

/**
 * PATCH semantics: fields optional. Service will validate when provided.
 */
@Data
public class AccountUpdateRequest {
    private String holderName; // optional
    private String status;     // optional: expect "ACTIVE" or "INACTIVE"
}
