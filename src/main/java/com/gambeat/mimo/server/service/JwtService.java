package com.gambeat.mimo.server.service;


import com.gambeat.mimo.server.model.User;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {

    String createToken(User user);
    Claims decodeToken(String token);
}
