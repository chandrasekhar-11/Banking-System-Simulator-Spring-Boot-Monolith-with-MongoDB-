package com.bankingsystem.simulator.service;

import com.bankingsystem.simulator.model.dto.DepositRequest;
import com.bankingsystem.simulator.model.dto.WithdrawRequest;
import com.bankingsystem.simulator.model.dto.TransferRequest;
import com.bankingsystem.simulator.model.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse deposit(String accountNumber, DepositRequest request);

    TransactionResponse withdraw(String accountNumber, WithdrawRequest request);

    TransactionResponse transfer(TransferRequest request);

    List<TransactionResponse> getTransactions(String accountNumber);
}
