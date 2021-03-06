package com.gambeat.mimo.server.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document
public class User extends Default {
    private String username;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private Enum.LoginType loginType;
    private String email;
    @DBRef
    private Wallet wallet;
    private Statistics statistics;
    private FacebookCredential facebookCredential;
    private ArrayList<String> pendingMatch = new ArrayList<>();


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

    public FacebookCredential getFacebookCredential() {
        return facebookCredential;
    }

    public void setFacebookCredential(FacebookCredential facebookCredential) {
        this.facebookCredential = facebookCredential;
    }

    public ArrayList<String> getPendingMatch() {
        return pendingMatch;
    }

    public void setPendingMatch(ArrayList<String> pendingMatch) {
        this.pendingMatch = pendingMatch;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
