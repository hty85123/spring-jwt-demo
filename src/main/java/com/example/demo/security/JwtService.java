package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

public class JwtService {
    private final SecretKey secretKey;
    private final int validSeconds;
    private final JwtParser jwtParser;

    public JwtService(String secretKeyStr, int validSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyStr.getBytes());
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
        this.validSeconds = validSeconds;
    }

    public String createLoginAccessToken(MemberUserDetails user) {
        var expirationMillis = Instant.now()
                .plusSeconds(validSeconds)
                .getEpochSecond()
                * 1000;

        var claims = Jwts.claims()
                .subject(user.getId())
                .issuedAt(new Date())
                .expiration(new Date(expirationMillis))
                .add("nickname", user.getNickname())
                .add("username", user.getUsername())
                .add("authorities", user.getMemberAuthorities())
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }

    public Claims parseToken(String jwt) throws JwtException {
        return jwtParser.parseSignedClaims(jwt).getPayload();
    }
}