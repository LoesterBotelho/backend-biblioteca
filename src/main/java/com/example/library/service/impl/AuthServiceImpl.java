package com.example.library.service.impl;

import com.example.library.dto.request.LoginRequest;
import com.example.library.dto.request.RefreshTokenRequest;
import com.example.library.dto.response.AuthResponse;
import com.example.library.exception.BusinessException;
import com.example.library.security.JwtTokenProvider;
import com.example.library.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {


    private static final Logger log =
            LoggerFactory.getLogger(AuthServiceImpl.class);


    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomUserDetailsService userDetailsService;



    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            CustomUserDetailsService userDetailsService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }



    @Override
    public AuthResponse login(LoginRequest request) {


        log.info("=================================================");
        log.info("LOGIN REQUEST");
        log.info("Username: {}", request.getUsername());


        try {


            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(),
                                    request.getPassword()
                            )
                    );


            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();



            String role =
                    userDetails.getAuthorities()
                            .stream()
                            .findFirst()
                            .map(Object::toString)
                            .orElse("ROLE_USER");



            log.info("Authentication SUCCESS");
            log.info("User: {}", userDetails.getUsername());
            log.info("Role: {}", role);



            String accessToken =
                    jwtTokenProvider.createToken(
                            userDetails.getUsername(),
                            role
                    );



            String refreshToken =
                    jwtTokenProvider.createRefreshToken(
                            userDetails.getUsername()
                    );



            log.info("JWT generated successfully");
            log.info("=================================================");



            return new AuthResponse(
                    accessToken,
                    refreshToken
            );



        } catch (BadCredentialsException e) {


            log.error("Invalid username or password");


            throw new BusinessException(
                    "Invalid username or password"
            );


        } catch (DisabledException e) {


            log.error("User disabled");


            throw new BusinessException(
                    "User disabled"
            );


        } catch (LockedException e) {


            log.error("User locked");


            throw new BusinessException(
                    "User locked"
            );


        } catch (AuthenticationException e) {


            log.error(
                    "Authentication error: {}",
                    e.getMessage()
            );


            throw new BusinessException(
                    "Invalid username or password"
            );

        }

    }



    @Override
    public AuthResponse refreshToken(
            RefreshTokenRequest request
    ) {


        log.info("Refreshing token");



        String refreshToken =
                request.getRefreshToken();



        if (!jwtTokenProvider.validateToken(refreshToken)) {


            throw new BusinessException(
                    "Invalid refresh token"
            );

        }



        String username =
                jwtTokenProvider.getUsername(
                        refreshToken
                );



        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        username
                );



        String role =
                userDetails.getAuthorities()
                        .stream()
                        .findFirst()
                        .map(Object::toString)
                        .orElse("ROLE_USER");



        String accessToken =
                jwtTokenProvider.createToken(
                        username,
                        role
                );



        String newRefreshToken =
                jwtTokenProvider.createRefreshToken(
                        username
                );



        return new AuthResponse(
                accessToken,
                newRefreshToken
        );

    }

}