package com.gambeat.mimo.server.model.response;

public class WalletsAfricaBank {

    public WalletsAfricaBank(){}

    private String BankCode;
    private String BankName;
    private String BankSortCode;
    private String PaymentGateway;

    public String getBankCode() {
        return BankCode;
    }

    public void setBankCode(String bankCode) {
        BankCode = bankCode;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBankSortCode() {
        return BankSortCode;
    }

    public void setBankSortCode(String bankSortCode) {
        BankSortCode = bankSortCode;
    }

    public String getPaymentGateway() {
        return PaymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        PaymentGateway = paymentGateway;
    }
}
