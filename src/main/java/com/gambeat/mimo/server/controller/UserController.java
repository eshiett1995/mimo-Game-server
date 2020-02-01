package com.gambeat.mimo.server.controller;

import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.response.ProfileResponse;
import com.gambeat.mimo.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(produces = "application/json")
    public @ResponseBody
    ResponseEntity<ProfileResponse> getUserProfile() {
        User foundUser = userService.getUserByEmail("email").get();
        ProfileResponse profileResponse = new ProfileResponse(foundUser);
        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
    }
}
