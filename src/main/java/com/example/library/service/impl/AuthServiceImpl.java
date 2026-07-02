package com.example.library.service.impl;

import com.example.library.dto.request.LoginRequest;
import com.example.library.dto.request.RefreshTokenRequest;
import com.example.library.dto.response.AuthResponse;
import com.example.library.exception.BusinessException;
import com.example.library.security.JwtTokenProvider;
import com.example.library.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElse("ROLE_USER");
            String accessToken = jwtTokenProvider.createToken(userDetails.getUsername(), role);
            String refreshToken = jwtTokenProvider.createRefreshToken(userDetails.getUsername());
            return new AuthResponse(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new BusinessException("Invalid username or password");
        }
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            throw new BusinessException("Invalid refresh token");
        }
        String username = jwtTokenProvider.getUsername(request.getRefreshToken());
        userDetailsService.loadUserByUsername(username);
        String accessToken = jwtTokenProvider.createToken(username, "ROLE_USER");
        String refreshToken = jwtTokenProvider.createRefreshToken(username);
        return new AuthResponse(accessToken, refreshToken);
    }
}
