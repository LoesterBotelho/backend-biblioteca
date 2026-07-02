package com.example.library.service;

import com.example.library.dto.request.LoginRequest;
import com.example.library.dto.request.RefreshTokenRequest;
import com.example.library.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
}
