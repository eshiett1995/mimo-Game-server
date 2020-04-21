package com.gambeat.mimo.server.service;

import com.gambeat.mimo.server.model.Wallet;

public interface WalletService {
    Wallet save(Wallet wallet);
    boolean debit (Wallet wallet, long amount);
}
