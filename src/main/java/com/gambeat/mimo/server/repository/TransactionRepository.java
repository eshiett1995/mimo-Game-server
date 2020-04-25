package com.gambeat.mimo.server.repository;

import com.gambeat.mimo.server.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
