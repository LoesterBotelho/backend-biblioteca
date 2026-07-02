package com.example.library;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Bean
    CommandLineRunner testPassword(PasswordEncoder passwordEncoder) {
        return args -> {
            String raw = "Admin@123";
            String hash = passwordEncoder.encode(raw);

            System.out.println("RAW = " + raw);
            System.out.println("HASH = " + hash);
            System.out.println("MATCH = " + passwordEncoder.matches(raw, hash));
        };
    }
}