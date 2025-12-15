package com.bankingsystem.simulator.service.impl;

import com.bankingsystem.simulator.exception.AccountNotFoundException;
import com.bankingsystem.simulator.exception.InsufficientBalanceException;
import com.bankingsystem.simulator.exception.InvalidAmountException;
import com.bankingsystem.simulator.model.dto.*;
import com.bankingsystem.simulator.model.entity.Account;
import com.bankingsystem.simulator.model.entity.Transaction;
import com.bankingsystem.simulator.repository.AccountRepository;
import com.bankingsystem.simulator.repository.TransactionRepository;
import com.bankingsystem.simulator.service.TransactionService;
import com.bankingsystem.simulator.util.TransactionIdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public TransactionResponse deposit(String accountNumber, DepositRequest request) {

        log.info("Deposit request received for {} amount {}", accountNumber, request.getAmount());

        if (request.getAmount() == null || request.getAmount() <= 0)
            throw new InvalidAmountException("Amount must be greater than 0");

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        account.setBalance(account.getBalance() + request.getAmount());
        accountRepository.save(account);

        log.info("Deposit successful. New balance: {}", account.getBalance());

        Transaction txn = saveTransaction(
                "DEPOSIT",
                request.getAmount(),
                "SUCCESS",
                accountNumber,
                null
        );

        return toResponse(txn);
    }

    @Override
    public TransactionResponse withdraw(String accountNumber, WithdrawRequest request) {

        log.info("Withdraw request for {} amount {}", accountNumber, request.getAmount());

        if (request.getAmount() == null || request.getAmount() <= 0)
            throw new InvalidAmountException("Amount must be greater than 0");

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        if (account.getBalance() < request.getAmount())
            throw new InsufficientBalanceException("Insufficient balance");

        account.setBalance(account.getBalance() - request.getAmount());
        accountRepository.save(account);

        log.info("Withdrawal successful. New balance: {}", account.getBalance());

        Transaction txn = saveTransaction(
                "WITHDRAW",
                request.getAmount(),
                "SUCCESS",
                accountNumber,
                null
        );

        return toResponse(txn);
    }

    @Override
    public TransactionResponse transfer(TransferRequest request) {

        log.info("Transfer request: {} -> {} amount {}",
                request.getSourceAccount(),
                request.getDestinationAccount(),
                request.getAmount());

        if (request.getAmount() == null || request.getAmount() <= 0)
            throw new InvalidAmountException("Amount must be greater than 0");

        Account source = accountRepository.findByAccountNumber(request.getSourceAccount())
                .orElseThrow(() -> new AccountNotFoundException("Source account not found"));

        Account destination = accountRepository.findByAccountNumber(request.getDestinationAccount())
                .orElseThrow(() -> new AccountNotFoundException("Destination account not found"));

        if (source.getBalance() < request.getAmount())
            throw new InsufficientBalanceException("Insufficient funds");

        // Deduct from source
        source.setBalance(source.getBalance() - request.getAmount());
        accountRepository.save(source);

        // Add to destination
        destination.setBalance(destination.getBalance() + request.getAmount());
        accountRepository.save(destination);

        log.info("Transfer successful. Source balance: {}", source.getBalance());

        Transaction txn = saveTransaction(
                "TRANSFER",
                request.getAmount(),
                "SUCCESS",
                source.getAccountNumber(),
                destination.getAccountNumber()
        );

        return toResponse(txn);
    }

    @Override
    public List<TransactionResponse> getTransactions(String accountNumber) {

        log.info("Fetching all transactions for account {}", accountNumber);

        List<Transaction> txns = transactionRepository
                .findBySourceAccountOrDestinationAccount(accountNumber, accountNumber);

        return txns.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ---------------- Helper Methods ----------------

    private Transaction saveTransaction(String type, Double amount, String status, String source, String destination) {

        Transaction txn = Transaction.builder()
                .transactionId(TransactionIdGenerator.generate())
                .type(type)
                .amount(amount)
                .timestamp(new Date())
                .status(status)
                .sourceAccount(source)
                .destinationAccount(destination)
                .build();

        transactionRepository.save(txn);

        log.info("Transaction saved: {}", txn.getTransactionId());

        return txn;
    }

    private TransactionResponse toResponse(Transaction txn) {
        return TransactionResponse.builder()
                .transactionId(txn.getTransactionId())
                .type(txn.getType())
                .amount(txn.getAmount())
                .timestamp(txn.getTimestamp())
                .status(txn.getStatus())
                .build();
    }
}
