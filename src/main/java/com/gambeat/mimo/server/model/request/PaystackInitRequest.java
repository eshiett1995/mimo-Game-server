package com.gambeat.mimo.server.model.request;

public class PaystackInitRequest {

    public PaystackInitRequest() {}

    private String reference;
    private String amount;
    private String email;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
