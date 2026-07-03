package com.example.library.security;

import com.example.library.config.JwtProperties;
import io.jsonwebtoken.*;
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

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;

        String secret = jwtProperties.getSecret();

        System.out.println("JWT SECRET LENGTH: " + secret.length());

        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // =========================
    // CREATE TOKEN
    // =========================
    public String createToken(String username, String role) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getExpirationMs());

        return Jwts.builder()
                .setSubject(username)
                .addClaims(Map.of("role", role))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // =========================
    // VALIDATE TOKEN
    // =========================
    public boolean validateToken(String token) {
        try {
            System.out.println("VALIDATING TOKEN...");
            System.out.println("TOKEN: " + token);

            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            System.out.println("TOKEN VALID");
            return true;

        } catch (ExpiredJwtException e) {
            System.out.println("TOKEN EXPIRED");
        } catch (UnsupportedJwtException e) {
            System.out.println("UNSUPPORTED JWT");
        } catch (MalformedJwtException e) {
            System.out.println("MALFORMED JWT");
        } catch (SignatureException e) {
            System.out.println("INVALID SIGNATURE -> SECRET PROBLEM");
        } catch (Exception e) {
            System.out.println("JWT ERROR: " + e.getMessage());
        }

        return false;
    }

    // =========================
    // GET USERNAME
    // =========================
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // =========================
    // RESOLVE TOKEN
    // =========================
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        System.out.println("AUTH HEADER: " + bearerToken);

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}