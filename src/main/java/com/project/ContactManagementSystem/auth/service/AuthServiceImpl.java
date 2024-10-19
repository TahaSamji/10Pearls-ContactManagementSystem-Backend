package com.project.ContactManagementSystem.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.ContactManagementSystem.auth.dto.ChangePassRequest;
import com.project.ContactManagementSystem.auth.dto.LoginResponse;
import com.project.ContactManagementSystem.auth.mapper.AuthMapper;
import com.project.ContactManagementSystem.auth.models.User;
import com.project.ContactManagementSystem.auth.repositories.AuthRepository;
import com.project.ContactManagementSystem.dto.Response;

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
            return AuthMapper.UserToLoginResponse(user, true, "Success", GeneratedToken);
        } else {
            return AuthMapper.UserToLoginResponse(null, false, "Invalid Password", null);
        }
    }

    @Override
    public Response ChangePass(ChangePassRequest request, String token) {
        Response confirmation = new Response("");
        String email = jwtService.extractUserName(token);
        System.out.println(email);
        User user = authRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println(user.getName());
        System.out.println(user.getPassword());

        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            authRepository.save(user);
            confirmation.setMessage("Password has been Changed");
        } else {
            confirmation.setMessage("Old Password incorrect");
        }
        return confirmation;
    }

}
