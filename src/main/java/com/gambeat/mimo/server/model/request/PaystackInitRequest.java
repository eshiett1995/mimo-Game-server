package com.gambeat.mimo.server.model.request;

public class PaystackInitRequest {

    public PaystackInitRequest() {}

    private String reference;
    private long amount;
    private String email;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
