package com.gambeat.mimo.server.model.request;

public class WalletNgTransferRequest {


    public WalletNgTransferRequest(){}

  private String SecretKey;
  private String BankCode;
  private String AccountNumber;
  private String AccountName;
  private String TransactionReference;
  private String Amount;
  private String Narration;

    public String getSecretKey() {
        return SecretKey;
    }

    public void setSecretKey(String secretKey) {
        SecretKey = secretKey;
    }

    public String getBankCode() {
        return BankCode;
    }

    public void setBankCode(String bankCode) {
        BankCode = bankCode;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getTransactionReference() {
        return TransactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        TransactionReference = transactionReference;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getNarration() {
        return Narration;
    }

    public void setNarration(String narration) {
        Narration = narration;
    }
}
