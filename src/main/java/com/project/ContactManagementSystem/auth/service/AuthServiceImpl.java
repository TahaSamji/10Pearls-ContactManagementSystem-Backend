package com.project.ContactManagementSystem.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.ContactManagementSystem.auth.dto.LoginResponse;
import com.project.ContactManagementSystem.auth.mapper.AuthMapper;
import com.project.ContactManagementSystem.auth.models.User;
import com.project.ContactManagementSystem.auth.repositories.AuthRepository;


@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return authRepository.save(user);
    }

    @Override
    public LoginResponse login(User inputUser) {
        User user = authRepository.findByEmail(inputUser.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(inputUser.getPassword(), user.getPassword())) {
            String GeneratedToken = jwtService.generateToken(user);
            return AuthMapper.UserToLoginResponse(user,true,"Success",GeneratedToken);
        } else {
            return AuthMapper.UserToLoginResponse(null,false,"Invalid Password",null);
        }
    }

    
}
