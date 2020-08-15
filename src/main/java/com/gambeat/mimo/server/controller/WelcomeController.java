package com.gambeat.mimo.server.controller;

import com.gambeat.mimo.server.model.GambeatSystem;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.Wallet;
import com.gambeat.mimo.server.model.response.ResponseModel;
import com.gambeat.mimo.server.service.GambeatSystemService;
import com.gambeat.mimo.server.service.UserService;
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
import java.util.List;

@Controller
public class WelcomeController {

    @Value("${paystack.test.secretkey}")
    private String paystackTestSecretKey;

    final
    UserService userService;

    @Value("${environment}")
    private String environment;

    @Value("${paystack.test.publicKey}")
    private String paystackTestPublicKey;

    @Value("${payant.production.publicKey}")
    private String payantProductionPublicKey;

    @Value("${payant.test.publicKey}")
    private String payantTestPublicKey;

    private final WalletsAfricaService walletsAfricaService;

    public WelcomeController(WalletsAfricaService walletsAfricaService, UserService userService) {
        this.walletsAfricaService = walletsAfricaService;
        this.userService = userService;
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
        model.addAttribute("secretKey",environment.equals("sandbox") ? payantTestPublicKey : payantProductionPublicKey);
        return "payant";
    }

    @GetMapping(value = "/wallets.africa")
    public  ModelAndView getWalletsAfrica(ModelAndView modelAndView) {
        modelAndView.addObject("banks",walletsAfricaService.getBanks());
        modelAndView.setViewName("wallets.africa");
        return modelAndView;
    }
}
