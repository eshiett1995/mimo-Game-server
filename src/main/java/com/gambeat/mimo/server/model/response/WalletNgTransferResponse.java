package com.gambeat.mimo.server.model.response;

public class WalletNgTransferResponse extends ResponseModel {

    public WalletNgTransferResponse(boolean isSuccessful, String message){
        super(isSuccessful,message);
    }

    public WalletNgTransferResponse(){}

    private String ResponseCode;
    private  String Message;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    @Override
    public String getMessage() {
        return Message;
    }

    @Override
    public void setMessage(String message) {
        Message = message;
    }
}
