package com.gambeat.mimo.server.controller;
import com.gambeat.mimo.server.model.request.WalletNgTransferRequest;
import com.gambeat.mimo.server.model.response.WalletNgTransferResponse;
import com.gambeat.mimo.server.service.JwtService;
import com.gambeat.mimo.server.service.UserService;
import com.google.gson.Gson;
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

    @PostMapping(path="/wallets.ng", produces = "application/json")
    public @ResponseBody
    ResponseEntity<WalletNgTransferResponse> WalletNgTransfer(HttpServletRequest request, @RequestBody WalletNgTransferRequest walletNgTransferRequest) {
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new WalletNgTransferResponse(false, "User not authorized"), HttpStatus.OK);
        }

        Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));

        walletNgTransferRequest.setSecretKey(walletNgTestSecretKey);
        walletNgTransferRequest.setTransactionReference("Wallet.NG " + UUID.randomUUID().toString());
        walletNgTransferRequest.setNarration("Payout from Gambeat");

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
}
