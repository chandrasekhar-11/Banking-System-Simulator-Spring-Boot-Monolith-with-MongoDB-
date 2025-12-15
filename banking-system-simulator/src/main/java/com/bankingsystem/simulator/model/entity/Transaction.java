package com.bankingsystem.simulator.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    private String transactionId;     // Example: TXN-20250101-001
    private String type;              // DEPOSIT, WITHDRAW, TRANSFER
    private Double amount;
    private Date timestamp;
    private String status;            // SUCCESS, FAILED

    private String sourceAccount;     // For transfer
    private String destinationAccount; // For transfer
}
