package com.gambeat.mimo.server.controller;


import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.GambeatSystem;
import com.gambeat.mimo.server.model.Transaction;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.request.PaystackInitRequest;
import com.gambeat.mimo.server.model.response.ResponseModel;
import com.gambeat.mimo.server.repository.GambeatSystemRepository;
import com.gambeat.mimo.server.service.GambeatSystemService;
import com.gambeat.mimo.server.service.JwtService;
import com.gambeat.mimo.server.service.TransactionService;
import com.gambeat.mimo.server.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/top-up")
public class TopUpController {

    @Autowired
    UserService userService;

    @Autowired
    GambeatSystemService gambeatSystemService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    JwtService jwtService;

    @Value("${paystack.test.secretkey}")
    private String paystackTestSecretKey;

    @PostMapping(path="/paystack", produces = "application/json")
    public @ResponseBody
    ResponseEntity<ResponseModel> initPaystackCredit(HttpServletRequest request, @RequestBody PaystackInitRequest paystackInitRequest) {
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new ResponseModel(false, "User not authorized"), HttpStatus.OK);
        }
        Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
        Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.getWallet().setBalance(user.getWallet().getBalance() + paystackInitRequest.getAmount());
            userService.update(user);
            Transaction transaction = new Transaction();
            transaction.setAmount(paystackInitRequest.getAmount());
            transaction.setCreditWallet(user.getWallet());
            transaction.setPaymentOption(Enum.PaymentOption.Topup);
            transaction.setTransactionType(Enum.TransactionType.Credit);
            transaction.setReference(paystackInitRequest.getReference());
            transaction.setVendor(Enum.Vendor.Paystack);
            transactionService.save(transaction);
            return new ResponseEntity<>(new ResponseModel(true, "Wallet Successfully updated."), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new ResponseModel(false, "No user found"), HttpStatus.OK);
        }
    }
}
