package com.gambeat.mimo.server.controller;


import com.gambeat.mimo.server.model.*;
import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.request.FacebookLoginRequest;
import com.gambeat.mimo.server.model.response.ResponseModel;
import com.gambeat.mimo.server.service.JwtService;
import com.gambeat.mimo.server.service.UserService;
import com.gambeat.mimo.server.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class LoginController {

    final
    UserService userService;
    final
    JwtService jwtService;
    final
    WalletService walletService;

    public LoginController(UserService userService, JwtService jwtService, WalletService walletService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.walletService = walletService;
    }

    @PostMapping(value = "/facebook", produces = "application/json") public @ResponseBody
    ResponseEntity<ResponseModel> getEvent(@RequestBody FacebookLoginRequest faceBookLoginRequest) {
        try {
            User user = new User();
            ResponseModel responseModel = new ResponseModel();
            Optional<User> optionalUser = userService.findExistingFacebookUser(faceBookLoginRequest.getId(), faceBookLoginRequest.getEmail());

            if (optionalUser.isPresent()) {
                user = optionalUser.get();
                user.getFacebookCredential().setPhotoUrl(faceBookLoginRequest.getPhotoUrl());
                user.setPhotoUrl(faceBookLoginRequest.getPhotoUrl());
                userService.update(user);

                responseModel.setMessage("Log in is successful");
                responseModel.setJtwToken(jwtService.createToken(optionalUser.get()));
                responseModel.setSuccessful(true);
                return new ResponseEntity<>(responseModel, HttpStatus.OK);
            } else {

                FacebookCredential facebookCredential = new FacebookCredential();
                facebookCredential.setEmail(faceBookLoginRequest.getEmail());
                facebookCredential.setId(faceBookLoginRequest.getId());
                facebookCredential.setFirstName(faceBookLoginRequest.getFirstName());
                facebookCredential.setLastName(faceBookLoginRequest.getLastName());
                facebookCredential.setPhotoUrl(faceBookLoginRequest.getPhotoUrl());

                user.setFirstName(faceBookLoginRequest.getFirstName());
                user.setLastName(faceBookLoginRequest.getLastName());
                user.setEmail(faceBookLoginRequest.getEmail());
                user.setLoginType(Enum.LoginType.Facebook);
                user.setFacebookCredential(facebookCredential);
                user.setPhotoUrl(faceBookLoginRequest.getPhotoUrl());

                user.setStatistics(new Statistics());


                Wallet createdWallet = walletService.save(new Wallet());
                user.setWallet(createdWallet);

                User savedUser = userService.save(user);

                responseModel.setMessage("Registration is successful");
                responseModel.setJtwToken(jwtService.createToken(savedUser));
                responseModel.setSuccessful(true);
                return new ResponseEntity<>(responseModel, HttpStatus.OK);
            }
        }catch (Exception exception){
            System.out.println(exception.getCause().getMessage());
            return new ResponseEntity<>(new ResponseModel(false, "An error occurred registering user"), HttpStatus.OK);
        }
    }
}
