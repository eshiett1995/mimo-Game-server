package com.gambeat.mimo.server.model.response;

import com.gambeat.mimo.server.model.Match;

public class MatchEntryResponse extends ResponseModel {

    public MatchEntryResponse(boolean isSuccessful, String message){
        super(isSuccessful, message);
    }
    public MatchEntryResponse(Match match){ }

}
