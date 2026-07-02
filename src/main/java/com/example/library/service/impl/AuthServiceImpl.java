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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        log.info("=================================================");
        log.info("LOGIN REQUEST");
        log.info("Username: {}", request.getUsername());
        log.info("Password Length: {}", request.getPassword() == null ? 0 : request.getPassword().length());

        try {

            log.info("Authenticating user...");

            UserDetails details =
                    userDetailsService.loadUserByUsername(request.getUsername());

            log.info("User found.");
            log.info("Username: {}", details.getUsername());
            log.info("Authorities: {}", details.getAuthorities());
            log.info("Enabled: {}", details.isEnabled());
            log.info("Account Non Locked: {}", details.isAccountNonLocked());
            log.info("Account Non Expired: {}", details.isAccountNonExpired());
            log.info("Credentials Non Expired: {}", details.isCredentialsNonExpired());

            // 🔥 DEBUG PASSWORD CHECK BLOCK
            log.info("DEBUG PASSWORD CHECK START");

            String raw = request.getPassword();
            String hash = details.getPassword();

            log.info("RAW = {}", raw);
            log.info("HASH = {}", hash);

            boolean match = passwordEncoder.matches(raw, hash);

            log.info("MATCH = {}", match);

            log.info("DEBUG PASSWORD CHECK END");

        } catch (Exception ex) {
            log.error("User NOT found: {}", ex.getMessage(), ex);
        }

        try {

            log.info("Calling AuthenticationManager...");

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(),
                                    request.getPassword()
                            )
                    );

            log.info("Authentication SUCCESS");

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            log.info("Authenticated User: {}", userDetails.getUsername());
            log.info("Authorities: {}", userDetails.getAuthorities());

            String role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElse("ROLE_USER");

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

            log.info("JWT generated successfully.");
            log.info("=================================================");

            return new AuthResponse(accessToken, refreshToken);

        } catch (BadCredentialsException e) {

            log.error("BAD CREDENTIALS");
            log.error("{}", e.getMessage(), e);

            throw new BusinessException("Invalid username or password");

        } catch (DisabledException e) {

            log.error("USER DISABLED");
            log.error("{}", e.getMessage(), e);

            throw new BusinessException("User disabled");

        } catch (LockedException e) {

            log.error("USER LOCKED");
            log.error("{}", e.getMessage(), e);

            throw new BusinessException("User locked");

        } catch (AuthenticationException e) {

            log.error("AUTHENTICATION ERROR");
            log.error("Class: {}", e.getClass().getName());
            log.error("Message: {}", e.getMessage(), e);

            throw new BusinessException("Invalid username or password");

        } catch (Exception e) {

            log.error("UNEXPECTED ERROR");
            log.error("Class: {}", e.getClass().getName());
            log.error("Message: {}", e.getMessage(), e);

            throw e;
        }
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {

        log.info("Refreshing token...");

        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            throw new BusinessException("Invalid refresh token");
        }

        String username =
                jwtTokenProvider.getUsername(request.getRefreshToken());

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(username);

        String role = userDetails.getAuthorities()
                .stream()
                .findFirst()
                .map(Object::toString)
                .orElse("ROLE_USER");

        String accessToken =
                jwtTokenProvider.createToken(username, role);

        String refreshToken =
                jwtTokenProvider.createRefreshToken(username);

        return new AuthResponse(accessToken, refreshToken);
    }
}