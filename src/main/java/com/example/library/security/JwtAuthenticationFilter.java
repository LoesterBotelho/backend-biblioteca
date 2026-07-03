package com.example.library.security;

import com.example.library.service.impl.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            CustomUserDetailsService userDetailsService) {

        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("\n================ JWT FILTER ================");
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("METHOD: " + request.getMethod());

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("OPTIONS request -> bypass filter");
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtTokenProvider.resolveToken(request);

        System.out.println("TOKEN PRESENT: " + (token != null));

        if (token != null) {
            System.out.println("TOKEN (partial): " +
                    token.substring(0, Math.min(20, token.length())) + "...");
        }

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {

                System.out.println("TOKEN VALID: true");

                String username = jwtTokenProvider.getUsername(token);
                System.out.println("USERNAME FROM TOKEN: " + username);

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                System.out.println("USER LOADED: " + userDetails.getUsername());

                System.out.println("AUTHORITIES:");
                userDetails.getAuthorities()
                        .forEach(a -> System.out.println(" - " + a.getAuthority()));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                System.out.println("AUTH SET IN SECURITY CONTEXT");
            } else {
                System.out.println("TOKEN INVALID OR NULL");
            }

        } catch (Exception e) {
            System.out.println("ERROR IN JWT FILTER: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("CURRENT AUTH: " +
                SecurityContextHolder.getContext().getAuthentication());

        System.out.println("===========================================\n");

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();

        boolean skip =
                path.startsWith("/api/v1/auth/") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/h2-console");

        if (skip) {
            System.out.println("SKIP FILTER FOR: " + path);
        }

        return skip;
    }
}