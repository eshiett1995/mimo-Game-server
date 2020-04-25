package com.gambeat.mimo.server.service.implementation;


import com.gambeat.mimo.server.model.Transaction;
import com.gambeat.mimo.server.model.Wallet;
import com.gambeat.mimo.server.service.TransactionService;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImplementation implements TransactionService {
    @Override
    public Transaction save(Transaction transaction) {
        return null;
    }

    @Override
    public Transaction saveEntryFeeTransaction(Wallet wallet, long amount) {
        return null;
    }

    @Override
    public Transaction saveGambeatFeeTransaction(Wallet wallet, long ammout) {
        return null;
    }
}
