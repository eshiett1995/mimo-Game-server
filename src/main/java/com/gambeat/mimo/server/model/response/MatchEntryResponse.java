package com.gambeat.mimo.server.model.response;

import com.gambeat.mimo.server.model.Match;

public class MatchEntryResponse extends ResponseModel {

    public MatchEntryResponse(boolean isSuccessful, String message){
        this.isSuccessful = isSuccessful;
        this.message = message;
    }
    public MatchEntryResponse(Match match){ }

    private boolean isSuccessful;
    private String message;

    @Override
    public boolean isSuccessful() {
        return isSuccessful;
    }

    @Override
    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
