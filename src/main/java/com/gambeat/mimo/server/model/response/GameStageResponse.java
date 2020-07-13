package com.gambeat.mimo.server.model.response;

public class GameStageResponse extends ResponseModel {

    private String data;

    public GameStageResponse(boolean successful, String message){
        super(successful,message);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
