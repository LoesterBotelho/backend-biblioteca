package com.example.library.debug;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class PasswordDebugService {

    private final PasswordEncoder passwordEncoder;

    public PasswordDebugService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void test() {
        String raw = "admin";

        String hash = passwordEncoder.encode(raw);

        System.out.println("HASH = " + hash);

        System.out.println("MATCH = " +
                passwordEncoder.matches(raw, hash));
    }
}