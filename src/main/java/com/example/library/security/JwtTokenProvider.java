package com.example.library.security;


import com.example.library.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import io.jsonwebtoken.security.Keys;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;



@Component
public class JwtTokenProvider {


    private final JwtProperties jwtProperties;


    private final SecretKey secretKey;



    public JwtTokenProvider(
            JwtProperties jwtProperties
    ) {

        this.jwtProperties = jwtProperties;


        String secret =
                jwtProperties.getSecret();



        this.secretKey =
                Keys.hmacShaKeyFor(
                        secret.getBytes(
                                StandardCharsets.UTF_8
                        )
                );

    }




    // ==================================================
    // ACCESS TOKEN
    // ==================================================

    public String createToken(
            String username,
            String role
    ) {


        Date now =
                new Date();



        Date expiry =
                new Date(
                        now.getTime()
                                +
                        jwtProperties.getExpirationMs()
                );



        return Jwts.builder()

                .setSubject(username)

                .addClaims(
                        Map.of(
                                "role",
                                role
                        )
                )

                .setIssuedAt(now)

                .setExpiration(expiry)

                .signWith(
                        secretKey,
                        SignatureAlgorithm.HS256
                )

                .compact();

    }




    // ==================================================
    // REFRESH TOKEN
    // ==================================================

    public String createRefreshToken(
            String username
    ) {


        Date now =
                new Date();



        Date expiry =
                new Date(
                        now.getTime()
                                +
                        jwtProperties.getRefreshExpirationMs()
                );



        return Jwts.builder()

                .setSubject(username)

                .setIssuedAt(now)

                .setExpiration(expiry)

                .signWith(
                        secretKey,
                        SignatureAlgorithm.HS256
                )

                .compact();

    }





    // ==================================================
    // VALIDATE TOKEN
    // ==================================================

    public boolean validateToken(
            String token
    ) {


        try {


            Jwts.parserBuilder()

                    .setSigningKey(secretKey)

                    .build()

                    .parseClaimsJws(token);



            return true;



        } catch (
                ExpiredJwtException |
                UnsupportedJwtException |
                MalformedJwtException |
                SignatureException |
                IllegalArgumentException e
        ) {


            return false;

        }

    }





    // ==================================================
    // GET USERNAME
    // ==================================================

    public String getUsername(
            String token
    ) {


        Claims claims =
                Jwts.parserBuilder()

                        .setSigningKey(secretKey)

                        .build()

                        .parseClaimsJws(token)

                        .getBody();



        return claims.getSubject();

    }





    // ==================================================
    // RESOLVE TOKEN
    // ==================================================

    public String resolveToken(
            HttpServletRequest request
    ) {


        String bearerToken =
                request.getHeader(
                        "Authorization"
                );



        if (
                bearerToken != null
                &&
                bearerToken.startsWith("Bearer ")
        ) {


            return bearerToken.substring(7);

        }



        return null;

    }

}