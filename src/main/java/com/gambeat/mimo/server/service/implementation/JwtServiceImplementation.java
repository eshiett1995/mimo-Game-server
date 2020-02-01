package com.gambeat.mimo.server.service.implementation;

import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class JwtServiceImplementation implements JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;


    @Override
    public String createToken(User user) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(jwtSecret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Map<String, Object> claims = new HashMap();
        claims.put("provider", user.getLoginType() == Enum.LoginType.Facebook ? "facebook" : "google");
        claims.put("provider_credential", user.getLoginType() == Enum.LoginType.Facebook ? user.getFacebookCredential() : "google");
        claims.put("email", user.getEmail());
        //Let's set the JWT Claims
        System.out.println("this is the email " + user.getEmail());
        JwtBuilder builder = Jwts.builder().setId(user.getEmail())
                .setIssuedAt(now)

                .setClaims(claims)
                .setIssuer("Gambeat")
                .setSubject("Auth")
                .signWith(signingKey, signatureAlgorithm);

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();

    }

    @Override
    public Claims decodeToken(String token) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(jwtSecret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token).getBody();
        return claims;
    }

}
