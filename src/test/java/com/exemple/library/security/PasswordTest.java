package com.exemple.library.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {

    @Test
    void testPasswordMatchAdmin() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        boolean matches = encoder.matches(
                "Admin@123",
                "$2a$10$7q0v0Jm0qQzqYw8d1rKqUe0Q7mZ7sZfQp1f1oJb0l8QeQm2g0m8x2"
        );

        System.out.println("Admin : Password matches: " + matches);
    }

    @Test
    void testPasswordMatchStudent() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        boolean matches = encoder.matches(
                "Student@123",
                "$2a$10$N9k8v2pX9cQw3mR8tY1fQeK0aZpL7sD8vB2nH5xC9mQ1eR6tY7uI0"
        );

        System.out.println("Student : Password matches: " + matches);
    }    
}