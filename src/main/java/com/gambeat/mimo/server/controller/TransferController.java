package com.gambeat.mimo.server.controller;
import com.gambeat.mimo.server.model.request.WalletNgTransferRequest;
import com.gambeat.mimo.server.model.response.WalletsAfricaBank;
import com.gambeat.mimo.server.model.response.WalletNgTransferResponse;
import com.gambeat.mimo.server.service.JwtService;
import com.gambeat.mimo.server.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.Claims;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/transfer")
public class TransferController {

    @Value("${walletng.test.secretkey}")
    private String walletNgTestSecretKey;
    @Value("${walletng.test.publickey}")
    private String walletNgTestPublicKey;

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @PostMapping(path="/wallets.africa", produces = "application/json")
    public @ResponseBody
    ResponseEntity<WalletNgTransferResponse> WalletNgTransfer(HttpServletRequest request, @RequestBody WalletNgTransferRequest walletNgTransferRequest) {
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new WalletNgTransferResponse(false, "User not authorized"), HttpStatus.OK);
        }



        Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));

        walletNgTransferRequest.setSecretKey(walletNgTestSecretKey);
        walletNgTransferRequest.setTransactionReference("Wallet.NG " + UUID.randomUUID().toString());
        walletNgTransferRequest.setNarration("Payout from Gambeat");

        System.out.println(new Gson().toJson(walletNgTransferRequest));

        try {

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
            okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, new Gson().toJson(walletNgTransferRequest));
            Request httpRequest = new Request.Builder()
                    .url("https://sandbox.wallets.africa/transfer/bank/account")
                    .method("POST", body)
                    .addHeader("Authorization", "Bearer " + walletNgTestPublicKey)
                    .build();
            Response httpResponse = client.newCall(httpRequest).execute();

            WalletNgTransferResponse walletNgTransferResponse;
            walletNgTransferResponse = new Gson().fromJson(httpResponse.body().string(), WalletNgTransferResponse.class);

            if(httpResponse.isSuccessful()){
                walletNgTransferResponse.setSuccessful(true);
            }else{
                walletNgTransferResponse.setSuccessful(false);
            }
            return new ResponseEntity<>(walletNgTransferResponse, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new WalletNgTransferResponse(false, "A server error has occured"), HttpStatus.OK);
        }
    }

    @GetMapping(path="/wallets.africa/banks", produces = "application/json")
    public @ResponseBody
    ResponseEntity<List<WalletsAfricaBank>> WalletTransfer(HttpServletRequest httpServletRequest) {
        System.out.println("thids is it");
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("text/plain");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://sandbox.wallets.africa/transfer/banks/all")
                .addHeader("Authorization", "Bearer " + walletNgTestPublicKey)
        //.addHeader("Access-Control-Allow-Origin", "your-host")
        .addHeader("Access-Control-Allow-Credentials", "true")
        .addHeader("Access-Control-Allow-Methods", "POST,GET")
        .addHeader("Access-Control-Allow-Headers", "application/json")
        .addHeader("Content-Type", "application/json")
                .method("POST", body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            List<WalletsAfricaBank> l = new Gson().fromJson(response.body().toString(),new TypeToken<List<WalletsAfricaBank>>() {}.getType());
            return new ResponseEntity<>(l, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
    }

}
