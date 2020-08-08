package com.gambeat.mimo.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @Value("${paystack.test.secretkey}")
    private String paystackTestSecretKey;

    @Value("${paystack.test.publicKey}")
    private String paystackTestPublicKey;

    @GetMapping(value = "/")
    public  String getWelcomePage() {
       return "index";
    }

    @GetMapping(value = "/payment")
    public  String getPaymentPage() {
        return "payment";
    }

    @GetMapping(value = "/paystack")
    public  String getPaystackPage(Model model) {
        model.addAttribute("secretKey",paystackTestPublicKey);
        return "paystack";
    }

    @GetMapping(value = "/wallets.africa")
    public  String getWalletsAfica() {
        return "wallets.africa";
    }
}
