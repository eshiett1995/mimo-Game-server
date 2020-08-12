package com.gambeat.mimo.server.controller;

import com.gambeat.mimo.server.service.WalletsAfricaService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class WelcomeController {

    @Value("${paystack.test.secretkey}")
    private String paystackTestSecretKey;

    @Value("${paystack.test.publicKey}")
    private String paystackTestPublicKey;

    @Value("${payant.production.publicKey}")
    private String payantProductionPublicKey;

    private final WalletsAfricaService walletsAfricaService;

    public WelcomeController(WalletsAfricaService walletsAfricaService) {
        this.walletsAfricaService = walletsAfricaService;
    }

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

    @GetMapping(value = "/payant")
    public  String getPayantPage(Model model) {
        model.addAttribute("secretKey",payantProductionPublicKey);
        return "payant";
    }

    @GetMapping(value = "/wallets.africa")
    public  ModelAndView getWalletsAfrica(ModelAndView modelAndView) {
        modelAndView.addObject("banks",walletsAfricaService.getBanks());
        modelAndView.setViewName("wallets.africa");
        return modelAndView;
    }
}
