package com.project.contactmanagementsystem.auth.config;

import java.io.IOException;


import com.project.contactmanagementsystem.auth.customexceptions.SystemErrorException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.contactmanagementsystem.auth.customexceptions.UserNotFoundException;
import com.project.contactmanagementsystem.auth.models.User;
import com.project.contactmanagementsystem.auth.repositories.AuthRepository;
import com.project.contactmanagementsystem.auth.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    private final AuthRepository authRepository;

    public JwtFilter(JwtService jwtService, AuthRepository authRepository) {
        this.jwtService = jwtService;
        this.authRepository = authRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token;
        String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            token = authHeader.substring(7);
            userEmail = jwtService.extractEmail(token);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                User userDetails = authRepository.findByEmail(userEmail)
                        .orElseThrow(UserNotFoundException::new);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }
            filterChain.doFilter(request, response);

        } catch (ServletException | IOException e) {
            throw new SystemErrorException(e.getMessage());
        }

    }

}
