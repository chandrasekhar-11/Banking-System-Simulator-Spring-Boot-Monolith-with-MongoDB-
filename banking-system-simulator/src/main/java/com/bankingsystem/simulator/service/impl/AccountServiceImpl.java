package com.bankingsystem.simulator.service.impl;

import com.bankingsystem.simulator.exception.AccountNotFoundException;
import com.bankingsystem.simulator.exception.InvalidInputException;
import com.bankingsystem.simulator.model.dto.AccountCreateRequest;
import com.bankingsystem.simulator.model.dto.AccountResponse;
import com.bankingsystem.simulator.model.dto.AccountUpdateRequest;
import com.bankingsystem.simulator.model.entity.Account;
import com.bankingsystem.simulator.repository.AccountRepository;
import com.bankingsystem.simulator.service.AccountService;
import com.bankingsystem.simulator.util.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountResponse createAccount(AccountCreateRequest request) {
        if (request.getHolderName() == null || request.getHolderName().trim().isEmpty()) {
            throw new InvalidInputException("Holder name cannot be empty");
        }

        log.info("Creating account for {}", request.getHolderName());
        String accNumber = AccountNumberGenerator.generate(request.getHolderName());

        Account account = Account.builder()
                .holderName(request.getHolderName().trim())
                .accountNumber(accNumber)
                .balance(0.0)
                .status("ACTIVE")
                .createdAt(new Date())
                .build();

        accountRepository.save(account);
        log.info("Account created: {}", accNumber);

        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .holderName(account.getHolderName())
                .balance(account.getBalance())
                .status(account.getStatus())
                .build();
    }

    @Override
    public AccountResponse getAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .holderName(account.getHolderName())
                .balance(account.getBalance())
                .status(account.getStatus())
                .build();
    }

    @Override
    public AccountResponse updateAccount(String accountNumber, AccountUpdateRequest request) {
        log.info("Update request for account {} : {}", accountNumber, request);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        boolean changed = false;

        // holderName validation and update
        if (request.getHolderName() != null) {
            String newName = request.getHolderName().trim();
            if (newName.isEmpty()) {
                throw new InvalidInputException("holderName, if provided, cannot be empty");
            }
            if (!newName.equals(account.getHolderName())) {
                account.setHolderName(newName);
                changed = true;
            }
        }

        // status validation and update
        if (request.getStatus() != null) {
            String s = request.getStatus().trim().toUpperCase();
            if (!s.equals("ACTIVE") && !s.equals("INACTIVE")) {
                throw new InvalidInputException("status must be either ACTIVE or INACTIVE");
            }
            if (!s.equals(account.getStatus())) {
                account.setStatus(s);
                changed = true;
            }
        }

        if (changed) {
            accountRepository.save(account);
            log.info("Account {} updated", accountNumber);
        } else {
            log.info("No changes detected for account {}", accountNumber);
        }

        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .holderName(account.getHolderName())
                .balance(account.getBalance())
                .status(account.getStatus())
                .build();
    }

    @Override
    public void deleteAccount(String accountNumber) {
        log.info("Delete (soft) request for account {}", accountNumber);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        if ("INACTIVE".equalsIgnoreCase(account.getStatus())) {
            log.info("Account {} already INACTIVE", accountNumber);
            return;
        }

        account.setStatus("INACTIVE");
        accountRepository.save(account);

        log.info("Account {} marked INACTIVE", accountNumber);
    }
}
