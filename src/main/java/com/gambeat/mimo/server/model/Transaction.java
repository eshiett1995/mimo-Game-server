package com.gambeat.mimo.server.model;


import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Transaction extends Default {

    private long amount;

    private Enum.TransactionType transactionType;

    public Enum.PaymentOption paymentOption;

    @DBRef
    private Wallet debitWallet;

    @DBRef
    private Wallet creditWallet;


    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Enum.TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Enum.TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Enum.PaymentOption getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(Enum.PaymentOption paymentOption) {
        this.paymentOption = paymentOption;
    }

    public Wallet getDebitWallet() {
        return debitWallet;
    }

    public void setDebitWallet(Wallet debitWallet) {
        this.debitWallet = debitWallet;
    }

    public Wallet getCreditWallet() {
        return creditWallet;
    }

    public void setCreditWallet(Wallet creditWallet) {
        this.creditWallet = creditWallet;
    }
}
