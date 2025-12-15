package com.bankingsystem.simulator.controller;

import com.bankingsystem.simulator.model.dto.AccountCreateRequest;
import com.bankingsystem.simulator.model.dto.AccountResponse;
import com.bankingsystem.simulator.model.dto.AccountUpdateRequest;
import com.bankingsystem.simulator.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        return new ResponseEntity<>(accountService.createAccount(request), HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccount(accountNumber));
    }

    @PatchMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable String accountNumber,
            @RequestBody AccountUpdateRequest request) { // fields optional
        return ResponseEntity.ok(accountService.updateAccount(accountNumber, request));
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }
}
