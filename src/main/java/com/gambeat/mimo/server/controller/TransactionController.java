package com.gambeat.mimo.server.controller;


import com.gambeat.mimo.server.model.response.PaystackInitResponse;
import com.gambeat.mimo.server.service.JwtService;
import com.gambeat.mimo.server.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @GetMapping(path="/init/paystack", produces = "application/json")
    public @ResponseBody
    ResponseEntity<PaystackInitResponse> initPaysackDebit(HttpServletRequest request) {
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new PaystackInitResponse(false, "User not authorized"), HttpStatus.OK);
        }
        try {
            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));

            RestTemplate restTemplate = new RestTemplate();

            HttpEntity<PaystackInitResponse> body = new HttpEntity<>(new PaystackInitResponse());
            PaystackInitResponse paystackInitResponse = restTemplate.postForObject(" https://api.paystack.co/transaction/initialize", body, PaystackInitResponse.class);

            return new ResponseEntity<>(paystackInitResponse, HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(new PaystackInitResponse(false, "Oops! failed to fetch user profile"), HttpStatus.OK);
        }
    }
}
