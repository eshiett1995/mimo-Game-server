package com.gambeat.mimo.server.model.response;

public class PaystackInitResponse extends ResponseModel{

    public PaystackInitResponse(Boolean isSuccessful, String msg){
        super(isSuccessful, msg);
    }

    public PaystackInitResponse(){}

    private boolean status;

    //private Data data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

//    public Data getData() {
//        return data;
//    }
//
//    public void setData(Data data) {
//        this.data = data;
//    }

    class Data {

        private String authorization_url;
        private String access_code;
        private String reference;

        private Data(){}

        public String getAuthorization_url() {
            return authorization_url;
        }

        public void setAuthorization_url(String authorization_url) {
            this.authorization_url = authorization_url;
        }

        public String getAccess_code() {
            return access_code;
        }

        public void setAccess_code(String access_code) {
            this.access_code = access_code;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }
    }
}
