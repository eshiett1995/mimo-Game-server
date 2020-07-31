package com.gambeat.mimo.server.service.implementation;


import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.Transaction;
import com.gambeat.mimo.server.model.Wallet;
import com.gambeat.mimo.server.repository.TransactionRepository;
import com.gambeat.mimo.server.service.GambeatSystemService;
import com.gambeat.mimo.server.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionServiceImplementation implements TransactionService {

    @Autowired
    TransactionService transactionService;

    @Autowired
    GambeatSystemService gambeatSystemService;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction saveEntryFeeTransaction(Wallet userWallet, long amount) {
        try {
            Wallet gambeatWallet = gambeatSystemService.getGambeatWallet();
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDebitWallet(userWallet);
            transaction.setCreditWallet(gambeatWallet);
            transaction.setPaymentOption(Enum.PaymentOption.EntryFee);
            transaction.setReference(UUID.randomUUID().toString());
            transaction.setTransactionType(Enum.TransactionType.Credit);
            transaction.setVendor(Enum.Vendor.Gambeat);
            return transactionService.save(transaction);
        }catch (Exception exception){
            return null;
        }
    }

    @Override
    public Transaction saveGambeatFeeTransaction(Wallet userWallet, long amount) {
        try {
            Wallet gambeatWallet = gambeatSystemService.getGambeatWallet();
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDebitWallet(userWallet);
            transaction.setCreditWallet(gambeatWallet);
            transaction.setPaymentOption(Enum.PaymentOption.GambeatFee);
            transaction.setReference(UUID.randomUUID().toString());
            transaction.setTransactionType(Enum.TransactionType.Credit);
            transaction.setVendor(Enum.Vendor.Gambeat);
            return transactionService.save(transaction);
        }catch (Exception exception){
            return null;
        }
    }

    @Override
    public Transaction saveCreditWinnerWithCashPriceTransaction(Wallet gambeatWallet, Wallet userWallet, long amount) {

        try {
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDebitWallet(gambeatWallet);
            transaction.setCreditWallet(userWallet);
            transaction.setPaymentOption(Enum.PaymentOption.RoyalRumbleWinnerCashPrice);
            transaction.setReference(UUID.randomUUID().toString());
            transaction.setTransactionType(Enum.TransactionType.Debit);
            transaction.setVendor(Enum.Vendor.Gambeat);
            return transactionService.save(transaction);
        }catch (Exception exception){
            return null;
        }
    }
}
