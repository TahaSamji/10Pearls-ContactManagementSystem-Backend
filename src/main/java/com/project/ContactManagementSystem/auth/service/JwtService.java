package com.project.ContactManagementSystem.auth.service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.project.ContactManagementSystem.auth.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private String secretKey;

    public JwtService() {
        try {

            KeyGenerator KeyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey Mykey = KeyGen.generateKey();
            this.secretKey = Base64.getEncoder().encodeToString(Mykey.getEncoded());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(User userDetails) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getId());

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userDetails.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 500))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] KeyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(KeyBytes);
    }

    public String extractUserName(String token) {
       
        return extractClaim(token,Claims::getSubject);
       
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver) {
       final Claims claims = extractAllClaims(token);
       return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
       return Jwts.parser()
       .verifyWith(getKey())
       .build()
       .parseSignedClaims(token)
       .getPayload();
    }

    public boolean validateToken(String token, User userDetails) {
       final String userEmail = extractUserName(token);
       return (userEmail.equals(userDetails.getEmail()) && !isTokenExpired(token));
       
    }

    private boolean isTokenExpired(String token) {
       return extractExpiration(token).before(new Date());
       
    }

    private Date extractExpiration(String token) {
      return extractClaim(token, Claims::getExpiration);
    }

}