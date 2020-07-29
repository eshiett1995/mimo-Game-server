package com.gambeat.mimo.server.service;

import com.gambeat.mimo.server.model.Transaction;
import com.gambeat.mimo.server.model.Wallet;

public interface TransactionService {

    Transaction save(Transaction transaction);

    Transaction saveEntryFeeTransaction(Wallet wallet, long amount);

    Transaction saveGambeatFeeTransaction(Wallet wallet, long amount);

    Transaction saveCreditWinnerWithCashPriceTransaction(Wallet gambeatWallet, Wallet userWallet, long amount);
}
