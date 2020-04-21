package com.gambeat.mimo.server.model.request;

import com.gambeat.mimo.server.model.Enum;

public class MatchCreationRequest {
    private String matchName;
    private long entryFee;
    private Enum.MatchType matchType;

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
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
