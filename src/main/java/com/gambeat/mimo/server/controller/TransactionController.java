package com.gambeat.mimo.server.controller;


import com.gambeat.mimo.server.model.request.PaystackInitRequest;
import com.gambeat.mimo.server.model.response.PaystackInitResponse;
import com.gambeat.mimo.server.service.JwtService;
import com.gambeat.mimo.server.service.UserService;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @Value("${paystack.test.secretkey}")
    private String paystackTestSecretKey;

    @PostMapping(path="/init/paystack", produces = "application/json")
    public @ResponseBody
    ResponseEntity<PaystackInitResponse> initPaysackDebit(HttpServletRequest request, @RequestBody PaystackInitRequest paystackInitRequest) {
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new PaystackInitResponse(false, "User not authorized"), HttpStatus.OK);
        }

            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));

            paystackInitRequest.setReference(UUID.randomUUID().toString());
            paystackInitRequest.setEmail((String) claims.get("email"));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + paystackTestSecretKey);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<PaystackInitRequest> body = new HttpEntity<>(paystackInitRequest, headers);
            ResponseEntity<String> response = restTemplate.exchange("https://api.paystack.co/transaction/initialize",  HttpMethod.POST, body, String.class);
            PaystackInitResponse paystackInitResponse = new Gson().fromJson(response.getBody(), PaystackInitResponse.class);
            if(response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED){
                paystackInitResponse.setSuccessful(true);
            }else{
                paystackInitResponse.setSuccessful(false);
            }
            return new ResponseEntity<>(paystackInitResponse, HttpStatus.OK);
    }
}
