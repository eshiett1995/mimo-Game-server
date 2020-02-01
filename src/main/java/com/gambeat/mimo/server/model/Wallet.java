package com.gambeat.mimo.server.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Wallet extends Default {
    private long balance;

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
