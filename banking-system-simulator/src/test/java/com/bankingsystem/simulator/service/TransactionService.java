package com.bankingsystem.simulator.service;

import com.bankingsystem.simulator.exception.InsufficientBalanceException;
import com.bankingsystem.simulator.exception.InvalidAmountException;
import com.bankingsystem.simulator.model.dto.*;
import com.bankingsystem.simulator.model.entity.Account;
import com.bankingsystem.simulator.model.entity.Transaction;
import com.bankingsystem.simulator.repository.AccountRepository;
import com.bankingsystem.simulator.repository.TransactionRepository;
import com.bankingsystem.simulator.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void deposit_validAmount_shouldIncreaseBalanceAndSaveTransaction() {
        Account acc = Account.builder().accountNumber("JO1").balance(100.0).holderName("A").status("ACTIVE").build();
        when(accountRepository.findByAccountNumber("JO1")).thenReturn(Optional.of(acc));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        DepositRequest req = new DepositRequest();
        req.setAmount(50.0);

        TransactionResponse resp = transactionService.deposit("JO1", req);

        assertNotNull(resp);
        assertEquals("DEPOSIT", resp.getType());
        assertEquals(50.0, resp.getAmount());
        assertEquals(150.0, acc.getBalance()); // balance updated on entity
        verify(accountRepository, times(1)).save(acc);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void withdraw_insufficientFunds_shouldThrow() {
        Account acc = Account.builder().accountNumber("JO2").balance(100.0).holderName("B").status("ACTIVE").build();
        when(accountRepository.findByAccountNumber("JO2")).thenReturn(Optional.of(acc));

        WithdrawRequest req = new WithdrawRequest();
        req.setAmount(200.0);

        assertThrows(InsufficientBalanceException.class, () -> transactionService.withdraw("JO2", req));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void transfer_valid_shouldMoveFundsAndSaveTransaction() {
        Account src = Account.builder().accountNumber("SRC").balance(500.0).holderName("S").status("ACTIVE").build();
        Account dst = Account.builder().accountNumber("DST").balance(100.0).holderName("D").status("ACTIVE").build();

        when(accountRepository.findByAccountNumber("SRC")).thenReturn(Optional.of(src));
        when(accountRepository.findByAccountNumber("DST")).thenReturn(Optional.of(dst));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        TransferRequest req = new TransferRequest();
        req.setSourceAccount("SRC");
        req.setDestinationAccount("DST");
        req.setAmount(200.0);

        TransactionResponse resp = transactionService.transfer(req);

        assertNotNull(resp);
        assertEquals("TRANSFER", resp.getType());
        assertEquals(200.0, resp.getAmount());
        assertEquals(300.0, src.getBalance());
        assertEquals(300.0, dst.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void deposit_invalidAmount_shouldThrow() {
        DepositRequest req = new DepositRequest();
        req.setAmount(0.0);
        assertThrows(InvalidAmountException.class, () -> transactionService.deposit("ANY", req));
    }

    @Test
    void getTransactions_shouldReturnList() {
        Transaction t1 = Transaction.builder().transactionId("T1").type("DEPOSIT").amount(10.0).timestamp(new Date()).status("SUCCESS").sourceAccount("A").build();
        when(transactionRepository.findBySourceAccountOrDestinationAccount("A", "A")).thenReturn(List.of(t1));

        List<TransactionResponse> res = transactionService.getTransactions("A");
        assertEquals(1, res.size());
        assertEquals("T1", res.get(0).getTransactionId());
    }
}
