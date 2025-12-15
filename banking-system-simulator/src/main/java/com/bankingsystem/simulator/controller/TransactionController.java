package com.bankingsystem.simulator.controller;

import com.bankingsystem.simulator.model.dto.*;
import com.bankingsystem.simulator.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PutMapping("/{accountNumber}/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(transactionService.deposit(accountNumber, request));
    }


    @PutMapping("/{accountNumber}/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(transactionService.withdraw(accountNumber, request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transactionService.transfer(request));
    }


    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getTransactions(accountNumber));
    }
}
