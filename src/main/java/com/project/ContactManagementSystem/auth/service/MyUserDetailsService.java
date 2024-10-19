package com.project.ContactManagementSystem.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.ContactManagementSystem.auth.models.User;
import com.project.ContactManagementSystem.auth.repositories.AuthRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    public AuthRepository authRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = authRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if(user == null){
            throw new UsernameNotFoundException("User Not Found");
        }
        else{
            return user;
        }

    }

}
