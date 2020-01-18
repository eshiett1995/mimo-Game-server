package com.gambeat.mimo.server.repository;

import com.gambeat.mimo.server.model.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WalletRepository extends MongoRepository<Wallet, String> {
}
