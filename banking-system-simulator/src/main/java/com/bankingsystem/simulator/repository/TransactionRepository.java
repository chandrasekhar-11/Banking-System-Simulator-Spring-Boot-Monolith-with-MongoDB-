package com.bankingsystem.simulator.repository;

import com.bankingsystem.simulator.model.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findBySourceAccountOrDestinationAccount(String source, String dest);
}
