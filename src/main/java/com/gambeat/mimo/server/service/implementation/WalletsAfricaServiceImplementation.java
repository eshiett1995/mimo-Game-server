package com.gambeat.mimo.server.service.implementation;

import com.gambeat.mimo.server.model.request.WalletNgTransferRequest;
import com.gambeat.mimo.server.service.WalletsAfricaService;
import com.google.gson.Gson;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WalletsAfricaServiceImplementation implements WalletsAfricaService {

    @Value("${environment}")
    private String environment;

    @Value("${wallets.africa.test.publickey}")
    private String walletsAfricaTestPublicKey;

    @Value("${wallets.africa.production.publickey}")
    private String walletsAfricaProductionPublicKey;

    @Override
    public String getBanks() {
        String publicKey =  environment.equals("sandbox") ? walletsAfricaTestPublicKey : walletsAfricaProductionPublicKey;
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(environment.equals("sandbox") ?
                        "https://sandbox.wallets.africa/transfer/banks/all" :
                        "https://wallets.africa/transfer/banks/all"
                )
                .addHeader("Authorization", "Bearer " + publicKey)
                .method("POST", body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Response transferToBank(WalletNgTransferRequest walletNgTransferRequest) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, new Gson().toJson(walletNgTransferRequest));
        Request httpRequest = new Request.Builder()
                .url("https://sandbox.wallets.africa/transfer/bank/account")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + walletsAfricaTestPublicKey)
                .build();
        try {
             return client.newCall(httpRequest).execute();
        } catch (IOException e) {
            return null;
        }
    }
}
