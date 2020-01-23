package com.gambeat.mimo.server.model;

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private Enum.LoginType loginType;
    private String email;
    private Wallet wallet;
    private Statistics statistics;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Enum.LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(Enum.LoginType loginType) {
        this.loginType = loginType;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Statistics getStatistics() { return statistics;}

    public void setStatistics(Statistics statistics) {this.statistics = statistics; }
}