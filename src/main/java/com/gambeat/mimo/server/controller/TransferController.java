package com.gambeat.mimo.server.controller;
import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.Transaction;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.request.WalletNgTransferRequest;
import com.gambeat.mimo.server.model.response.ResponseModel;
import com.gambeat.mimo.server.model.response.WalletsAfricaTransferData;
import com.gambeat.mimo.server.service.*;
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
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/transfer")
public class TransferController {

    @Value("${wallets.africa.test.secretKey}")
    private String walletsAfricaTestSecretKey;
    @Value("${wallets.africa.test.publicKey}")
    private String walletsAfricaTestPublicKey;

    final
    UserService userService;

    final
    WalletsAfricaService walletsAfricaService;

    @Autowired
    WalletService walletService;

    final
    JwtService jwtService;

    final
    TransactionService transactionService;

    public TransferController(UserService userService, JwtService jwtService, TransactionService transactionService, WalletsAfricaService walletsAfricaService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.transactionService = transactionService;
        this.walletsAfricaService = walletsAfricaService;
    }

    @PostMapping(path="/wallets.africa", produces = "application/json")
    public @ResponseBody
    ResponseEntity<ResponseModel> WalletNgTransfer(HttpServletRequest request, @RequestBody WalletNgTransferRequest walletNgTransferRequest) {

        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new ResponseModel(false, "User not authorized"), HttpStatus.OK);
        }

        Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
        Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));

        if(!optionalUser.isPresent()){
            return new ResponseEntity<>(new ResponseModel(false, "No user present."), HttpStatus.OK);
        }

        User user = optionalUser.get();
        if(walletNgTransferRequest.getAmount()  > user.getWallet().getBalance()){
            return new ResponseEntity<>(new ResponseModel(false, "You do not have up to this in you wallet."), HttpStatus.OK);
        }

        walletNgTransferRequest.setSecretKey(walletsAfricaTestSecretKey);
        walletNgTransferRequest.setTransactionReference("Wallet.NG " + UUID.randomUUID().toString());
        walletNgTransferRequest.setNarration("Payout from Gambeat");


        try {


            Response httpResponse = walletsAfricaService.transferToBank(walletNgTransferRequest);

            WalletsAfricaTransferData walletsAfricaTransferData;
            walletsAfricaTransferData = new Gson().fromJson(httpResponse.body().string(), WalletsAfricaTransferData.class);
            ResponseModel responseModel = new ResponseModel();
            if(httpResponse.isSuccessful()){
                    user.getWallet().setBalance(user.getWallet().getBalance() - walletNgTransferRequest.getAmount());
                    walletService.update(user.getWallet());
                    Transaction transaction = new Transaction();
                    transaction.setAmount(walletNgTransferRequest.getAmount());
                    transaction.setCreditWallet(user.getWallet());
                    transaction.setPaymentOption(Enum.PaymentOption.Cashout);
                    transaction.setTransactionType(Enum.TransactionType.Credit);
                    transaction.setReference(walletNgTransferRequest.getTransactionReference());
                    transaction.setVendor(Enum.Vendor.WalletsAfrica);
                    transactionService.save(transaction);
                    return new ResponseEntity<>(new ResponseModel(true, walletsAfricaTransferData.getMessage()), HttpStatus.OK);
            }else{
                responseModel.setSuccessful(false);
                return new ResponseEntity<>(responseModel, HttpStatus.OK);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseModel(false, "A server error has occured"), HttpStatus.OK);
        }
    }
}
