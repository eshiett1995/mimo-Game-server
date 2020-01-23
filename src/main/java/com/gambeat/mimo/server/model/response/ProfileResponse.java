package com.gambeat.mimo.server.model.response;

import com.gambeat.mimo.server.model.User;

public class ProfileResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String loginProvider;
    private long walletBalance;
    private long wins;
    private long losses;
    private long draws;
    private long highestScore;


    public ProfileResponse(User user){
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.loginProvider = "Facebook";
        this.walletBalance = user.getWallet().getBalance();
        this.wins = user.getStatistics().getWins();
        this.losses = user.getStatistics().getLosses();
        this.draws = user.getStatistics().getDraws();
        this.highestScore = user.getStatistics().getHighestScore();

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

    public String getLoginProvider() {
        return loginProvider;
    }

    public void setLoginProvider(String loginProvider) {
        this.loginProvider = loginProvider;
    }

    public long getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(long walletBalance) {
        this.walletBalance = walletBalance;
    }

    public long getWins() {
        return wins;
    }

    public void setWins(long wins) {
        this.wins = wins;
    }

    public long getLosses() {
        return losses;
    }

    public void setLosses(long losses) {
        this.losses = losses;
    }

    public long getDraws() {
        return draws;
    }

    public void setDraws(long draws) {
        this.draws = draws;
    }

    public long getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(long highestScore) {
        this.highestScore = highestScore;
    }
}
