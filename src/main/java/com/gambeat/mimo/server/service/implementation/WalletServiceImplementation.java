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

    @Override
    public boolean debit(Wallet wallet, long amount) {
        try {
            wallet.setBalance(wallet.getBalance() - amount);
            this.save(wallet);
            return true;
        }catch (Exception error){
            return false;
        }
    }

    @Override
    public boolean credit(Wallet wallet, long amount) {
        try {
            wallet.setBalance(wallet.getBalance() + amount);
            this.save(wallet);
            return true;
        }catch (Exception error){
            return false;
        }
    }
}
