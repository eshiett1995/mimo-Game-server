package com.gambeat.mimo.server.model.request;

import com.gambeat.mimo.server.model.Enum;

import java.util.ArrayList;
import java.util.List;

public class MatchPlayedRequest {
    private String matchID;
    private String userID;
    private ArrayList<Integer> scores = new ArrayList<>();
    private long entryFee;
    private Enum.MatchType matchType;


    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<Integer> getScores() {
        return scores;
    }

    public void setScores(ArrayList<Integer> scores) {
        this.scores = scores;
    }

    public long getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(long entryFee) {
        this.entryFee = entryFee;
    }

    public Enum.MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(Enum.MatchType matchType) {
        this.matchType = matchType;
    }
}
