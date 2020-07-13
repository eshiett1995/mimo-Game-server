package com.gambeat.mimo.server.model.response;

public class ResponseModel {

    public ResponseModel(){}
    public ResponseModel(boolean isSuccessful, String message){
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    private String message;
    private boolean isSuccessful;
    private String jtwToken;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getJtwToken() {
        return jtwToken;
    }

    public void setJtwToken(String jtwToken) {
        this.jtwToken = jtwToken;
    }

}
