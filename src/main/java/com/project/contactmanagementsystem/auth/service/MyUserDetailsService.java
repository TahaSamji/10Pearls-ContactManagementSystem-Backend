package com.project.contactmanagementsystem.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.contactmanagementsystem.auth.customexceptions.UserNotFoundException;

import com.project.contactmanagementsystem.auth.repositories.AuthRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {


    private final AuthRepository authRepository;

    public MyUserDetailsService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

    }

}
