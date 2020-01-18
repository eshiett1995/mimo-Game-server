package com.gambeat.mimo.server.service.implementation;

import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;


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
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(user.getEmail())
                .setIssuedAt(now)
                .setSubject("Auth")
                .setIssuer("Gambeat")
                .signWith(signingKey, signatureAlgorithm);

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();

    }

    @Override
    public Claims decodeToken(String token) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecret))
                .parseClaimsJws(token).getBody();
        return claims;
    }

}
