package com.bankingsystem.simulator.service;

import com.bankingsystem.simulator.exception.AccountNotFoundException;
import com.bankingsystem.simulator.exception.InvalidInputException;
import com.bankingsystem.simulator.model.dto.AccountCreateRequest;
import com.bankingsystem.simulator.model.dto.AccountResponse;
import com.bankingsystem.simulator.model.dto.AccountUpdateRequest;
import com.bankingsystem.simulator.model.entity.Account;
import com.bankingsystem.simulator.repository.AccountRepository;
import com.bankingsystem.simulator.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    // existing create/get tests... (keep if present)

    @Test
    void updateAccount_whenHolderNameProvided_updatesName() {
        Account existing = Account.builder()
                .id("id1")
                .accountNumber("JO1234")
                .holderName("Old Name")
                .balance(100.0)
                .status("ACTIVE")
                .createdAt(new Date())
                .build();

        when(accountRepository.findByAccountNumber("JO1234")).thenReturn(Optional.of(existing));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        AccountUpdateRequest req = new AccountUpdateRequest();
        req.setHolderName("New Name");

        var resp = accountService.updateAccount("JO1234", req);

        assertNotNull(resp);
        assertEquals("New Name", resp.getHolderName());

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        assertEquals("New Name", captor.getValue().getHolderName());
    }

    @Test
    void updateAccount_whenInvalidHolderName_throwsInvalidInputException() {
        Account existing = Account.builder()
                .id("id2")
                .accountNumber("JO9999")
                .holderName("Name")
                .balance(50.0)
                .status("ACTIVE")
                .createdAt(new Date())
                .build();

        when(accountRepository.findByAccountNumber("JO9999")).thenReturn(Optional.of(existing));

        AccountUpdateRequest req = new AccountUpdateRequest();
        req.setHolderName("   "); // invalid (empty after trim)

        assertThrows(InvalidInputException.class, () -> accountService.updateAccount("JO9999", req));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void updateAccount_whenAccountNotFound_throwsAccountNotFoundException() {
        when(accountRepository.findByAccountNumber("NONEXIST")).thenReturn(Optional.empty());
        AccountUpdateRequest req = new AccountUpdateRequest();
        req.setHolderName("Any");

        assertThrows(AccountNotFoundException.class, () -> accountService.updateAccount("NONEXIST", req));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void deleteAccount_marksStatusInactive_whenActive() {
        Account existing = Account.builder()
                .id("id3")
                .accountNumber("DEL123")
                .holderName("To Delete")
                .balance(0.0)
                .status("ACTIVE")
                .createdAt(new Date())
                .build();

        when(accountRepository.findByAccountNumber("DEL123")).thenReturn(Optional.of(existing));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        accountService.deleteAccount("DEL123");

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());
        assertEquals("INACTIVE", captor.getValue().getStatus());
    }

    @Test
    void deleteAccount_whenAlreadyInactive_noSaveCalled() {
        Account existing = Account.builder()
                .id("id4")
                .accountNumber("DEL999")
                .holderName("Already Inactive")
                .balance(0.0)
                .status("INACTIVE")
                .createdAt(new Date())
                .build();

        when(accountRepository.findByAccountNumber("DEL999")).thenReturn(Optional.of(existing));

        accountService.deleteAccount("DEL999");

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void deleteAccount_whenAccountNotFound_throwsAccountNotFoundException() {
        when(accountRepository.findByAccountNumber("MISSING")).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount("MISSING"));
    }
}
