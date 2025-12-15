package com.bankingsystem.simulator.service;

import com.bankingsystem.simulator.model.dto.AccountCreateRequest;
import com.bankingsystem.simulator.model.dto.AccountResponse;
import com.bankingsystem.simulator.model.dto.AccountUpdateRequest;

public interface AccountService {
    AccountResponse createAccount(AccountCreateRequest request);
    AccountResponse getAccount(String accountNumber);
    AccountResponse updateAccount(String accountNumber, AccountUpdateRequest request);
    void deleteAccount(String accountNumber); // soft delete: mark INACTIVE
}
