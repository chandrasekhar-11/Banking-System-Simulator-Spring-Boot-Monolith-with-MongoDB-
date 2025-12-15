package com.bankingsystem.simulator.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "accounts")
public class Account {

    @Id
    private String id;

    private String accountNumber;
    private String holderName;
    private Double balance;
    private String status;   // ACTIVE or INACTIVE
    private Date createdAt;
}
