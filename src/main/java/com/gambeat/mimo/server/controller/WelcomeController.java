package com.gambeat.mimo.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
    @GetMapping(value = "/")
    public  String getWelcomePage() {
       return "index";
    }
}
