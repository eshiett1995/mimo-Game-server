package com.gambeat.mimo.server.controller;


import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.FacebookCredential;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.request.FacebookLoginRequest;
import com.gambeat.mimo.server.model.response.ResponseModel;
import com.gambeat.mimo.server.service.JwtService;
import com.gambeat.mimo.server.service.UserService;
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

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @PostMapping(value = "/facebook", produces = "application/json") public @ResponseBody
    ResponseEntity<ResponseModel> getEvent(@RequestBody FacebookLoginRequest faceBookLoginRequest) {
        User user = new User();
        ResponseModel responseModel = new ResponseModel();
        Optional<User>  optionalUser = userService.findExistingFacebookUser(faceBookLoginRequest.getId(), faceBookLoginRequest.getEmail());

        if(optionalUser.isPresent()){
            responseModel.setMessage("Log in is successful");
            responseModel.setJtwToken(jwtService.createToken(optionalUser.get()));
            responseModel.setSuccessful(true);
        }else{

            FacebookCredential facebookCredential = new FacebookCredential();
            facebookCredential.setEmail(faceBookLoginRequest.getEmail());
            facebookCredential.setId(faceBookLoginRequest.getId());
            facebookCredential.setFirstName(faceBookLoginRequest.getFirstName());
            facebookCredential.setLastName(faceBookLoginRequest.getLastName());

            user.setFirstName(faceBookLoginRequest.getFirstName());
            user.setLastName(faceBookLoginRequest.getLastName());
            user.setEmail(faceBookLoginRequest.getEmail());
            user.setLoginType(Enum.LoginType.Facebook);
            user.setFacebookCredential(facebookCredential);

            User savedUser = userService.save(user);

            responseModel.setMessage("Registration is successful");
            responseModel.setJtwToken(jwtService.createToken(savedUser));
            responseModel.setSuccessful(true);
        }

      return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }
}
