package com.gambeat.mimo.server.controller;


import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.Transaction;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.request.TopupInitRequest;
import com.gambeat.mimo.server.model.response.ResponseModel;
import com.gambeat.mimo.server.service.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/top-up")
public class TopUpController {

    final
    UserService userService;

    final
    WalletService walletService;

    final
    GambeatSystemService gambeatSystemService;

    final
    TransactionService transactionService;

    final
    JwtService jwtService;

    @Value("${paystack.test.secretKey}")
    private String paystackTestSecretKey;

    public TopUpController(UserService userService, GambeatSystemService gambeatSystemService, TransactionService transactionService, JwtService jwtService, WalletService walletService) {
        this.userService = userService;
        this.gambeatSystemService = gambeatSystemService;
        this.transactionService = transactionService;
        this.jwtService = jwtService;
        this.walletService = walletService;
    }

    @PostMapping(path="/paystack", produces = "application/json")
    public @ResponseBody
    ResponseEntity<ResponseModel> initPaystackCredit(HttpServletRequest request, @RequestBody TopupInitRequest topupInitRequest) {

        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new ResponseModel(false, "User not authorized"), HttpStatus.OK);
        }
        Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
        Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.getWallet().setBalance(user.getWallet().getBalance() + topupInitRequest.getAmount());
            walletService.save(user.getWallet());
            Transaction transaction = new Transaction();
            transaction.setAmount(topupInitRequest.getAmount());
            transaction.setCreditWallet(user.getWallet());
            transaction.setPaymentOption(Enum.PaymentOption.Topup);
            transaction.setTransactionType(Enum.TransactionType.Credit);
            transaction.setReference(topupInitRequest.getReference());
            transaction.setVendor(Enum.Vendor.Paystack);
            transactionService.save(transaction);
            return new ResponseEntity<>(new ResponseModel(true, "Wallet Successfully updated."), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new ResponseModel(false, "No user found"), HttpStatus.OK);
        }
    }

    @PostMapping(path="/payant", produces = "application/json")
    public @ResponseBody
    ResponseEntity<ResponseModel> initPayantCredit(HttpServletRequest request, @RequestBody TopupInitRequest topupInitRequest) {

        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new ResponseModel(false, "User not authorized"), HttpStatus.OK);
        }
        Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
        Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.getWallet().setBalance(user.getWallet().getBalance() + topupInitRequest.getAmount());
            walletService.save(user.getWallet());
            Transaction transaction = new Transaction();
            transaction.setAmount(topupInitRequest.getAmount());
            transaction.setCreditWallet(user.getWallet());
            transaction.setPaymentOption(Enum.PaymentOption.Topup);
            transaction.setTransactionType(Enum.TransactionType.Credit);
            transaction.setReference(topupInitRequest.getReference());
            transaction.setVendor(Enum.Vendor.Payant);
            transactionService.save(transaction);
            return new ResponseEntity<>(new ResponseModel(true, "Wallet Successfully updated."), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new ResponseModel(false, "No user found"), HttpStatus.OK);
        }
    }
}

//8999914300
