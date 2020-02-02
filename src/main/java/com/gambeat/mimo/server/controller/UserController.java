package com.gambeat.mimo.server.controller;

import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.response.LeaderBoardResponse;
import com.gambeat.mimo.server.model.response.ProfileResponse;
import com.gambeat.mimo.server.service.JwtService;
import com.gambeat.mimo.server.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @GetMapping(produces = "application/json")
    public @ResponseBody
    ResponseEntity<ProfileResponse> getUserProfile(HttpServletRequest request) {
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new ProfileResponse(false, "User not authorized"), HttpStatus.OK);
        }
        try {
            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
            Optional<User> optionalUser = userService.getUserByEmail((String) claims.get("email"));
            if(!optionalUser.isPresent()) return new ResponseEntity<>(new ProfileResponse(false, "User not found"), HttpStatus.OK);
            ProfileResponse profileResponse = new ProfileResponse(optionalUser.get());
            profileResponse.setMessage("Success");
            profileResponse.setSuccessful(true);
            return new ResponseEntity<>(profileResponse, HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(new ProfileResponse(false, "Oops! failed to fetch user profile"), HttpStatus.OK);
        }
    }

    @GetMapping(path="/{id}", produces = "application/json")
    public @ResponseBody
    ResponseEntity<ProfileResponse> getUserProfileById(HttpServletRequest request, @PathVariable("id") String id) {
        if(request.getHeader("Authorization") == null) {
            return new ResponseEntity<>(new ProfileResponse(false, "User not authorized"), HttpStatus.OK);
        }
        try {
            Claims claims = jwtService.decodeToken(request.getHeader("Authorization"));
            Optional<User> optionalUser = userService.getUserById(id);
            if(!optionalUser.isPresent()) return new ResponseEntity<>(new ProfileResponse(false, "User not found"), HttpStatus.OK);
            ProfileResponse profileResponse = new ProfileResponse(optionalUser.get());
            profileResponse.setWalletBalance(0);
            profileResponse.setMessage("Success");
            profileResponse.setSuccessful(true);
            return new ResponseEntity<>(profileResponse, HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(new ProfileResponse(false, "Oops! failed to fetch user profile"), HttpStatus.OK);
        }
    }
}
