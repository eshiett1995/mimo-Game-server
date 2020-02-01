package com.gambeat.mimo.server.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document
public class Match extends Default {
    private String name;
    private boolean isCompetition;
    private User winner;
    private Long entryFee;
    private ArrayList<User> winners;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompetition() {
        return isCompetition;
    }

    public void setCompetition(boolean competition) {
        isCompetition = competition;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public Long getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(Long entryFee) {
        this.entryFee = entryFee;
    }

    public ArrayList<User> getWinners() {
        return winners;
    }

    public void setWinners(ArrayList<User> winners) {
        this.winners = winners;
    }
}
