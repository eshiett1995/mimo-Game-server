package com.gambeat.mimo.server.service.implementation;

import com.gambeat.mimo.server.model.Wallet;
import com.gambeat.mimo.server.repository.WalletRepository;
import com.gambeat.mimo.server.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImplementation implements WalletService {

    @Autowired
    WalletRepository walletRepository;

    @Override
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }
}
