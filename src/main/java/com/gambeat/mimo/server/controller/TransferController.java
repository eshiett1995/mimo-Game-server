package com.gambeat.mimo.server.controller;
import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.Transaction;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.request.WalletNgTransferRequest;
import com.gambeat.mimo.server.model.response.ResponseModel;
import com.gambeat.mimo.server.model.response.WalletsAfricaBank;
import com.gambeat.mimo.server.model.response.WalletNgTransferResponse;
import com.gambeat.mimo.server.service.JwtService;
import com.gambeat.mimo.server.service.TransactionService;
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
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/transfer")
public class TransferController {

    @Value("${walletng.test.secretkey}")
    private String walletNgTestSecretKey;
    @Value("${walletng.test.publickey}")
    private String walletNgTestPublicKey;

    final
    UserService userService;

    final
    JwtService jwtService;

    final
    TransactionService transactionService;

    public TransferController(UserService userService, JwtService jwtService, TransactionService transactionService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.transactionService = transactionService;
    }

    @PostMapping(path="/wallets.africa", produces = "application/json")
    public @ResponseBody
    ResponseEntity<WalletNgTransferResponse> WalletNgTransfer(HttpServletRequest request, @RequestBody WalletNgTransferRequest walletNgTransferRequest) {

        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new WalletNgTransferResponse(false, "User not authorized"), HttpStatus.OK);
        }

        Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
        Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));

        if(!optionalUser.isPresent()){
            return new ResponseEntity<>(new WalletNgTransferResponse(false, "No user present."), HttpStatus.OK);
        }

        User user = optionalUser.get();
        if(user.getWallet().getBalance() > walletNgTransferRequest.getAmount()){
            return new ResponseEntity<>(new WalletNgTransferResponse(false, "You do not have up to this in you wallet."), HttpStatus.OK);
        }

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
                    user.getWallet().setBalance(user.getWallet().getBalance() - walletNgTransferRequest.getAmount());
                    userService.update(user);
                    Transaction transaction = new Transaction();
                    transaction.setAmount(walletNgTransferRequest.getAmount());
                    transaction.setCreditWallet(user.getWallet());
                    transaction.setPaymentOption(Enum.PaymentOption.Cashout);
                    transaction.setTransactionType(Enum.TransactionType.Credit);
                    transaction.setReference(walletNgTransferRequest.getTransactionReference());
                    transaction.setVendor(Enum.Vendor.WalletsAfrica);
                    transactionService.save(transaction);
                    return new ResponseEntity<>(new WalletNgTransferResponse(true, "Wallet Successfully updated."), HttpStatus.OK);
            }else{
                walletNgTransferResponse.setSuccessful(false);
                return new ResponseEntity<>(walletNgTransferResponse, HttpStatus.OK);
            }
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
