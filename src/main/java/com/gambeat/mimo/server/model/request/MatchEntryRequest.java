package com.gambeat.mimo.server.model.request;

import com.gambeat.mimo.server.model.Enum;

public class MatchEntryRequest {

    private long userID;
    private long entryFee;
    private Enum.MatchType matchType;

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
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
