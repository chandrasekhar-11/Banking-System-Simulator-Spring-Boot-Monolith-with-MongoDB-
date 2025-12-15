package com.bankingsystem.simulator.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountCreateRequest {

    @NotBlank(message = "Holder name cannot be empty")
    private String holderName;
}
