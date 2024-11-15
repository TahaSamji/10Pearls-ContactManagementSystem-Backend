package com.project.contactmanagementsystem.auth.service;

import com.project.contactmanagementsystem.auth.customexceptions.UserAlreadyExistsException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.contactmanagementsystem.auth.customexceptions.InvalidCredentialsException;
import com.project.contactmanagementsystem.auth.customexceptions.UserNotFoundException;
import com.project.contactmanagementsystem.auth.dto.ChangePassRequest;
import com.project.contactmanagementsystem.auth.dto.LoginResponse;
import com.project.contactmanagementsystem.auth.mapper.AuthMapper;
import com.project.contactmanagementsystem.auth.models.User;
import com.project.contactmanagementsystem.auth.repositories.AuthRepository;
import com.project.contactmanagementsystem.dto.Response;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {


    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthRepository authRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }



    @Override
    public User saveUser(User user) {
        User userExists = authRepository.findByemail(user.getEmail());

        if(userExists != null){
            log.error("User Already exist in the database");
            throw new UserAlreadyExistsException("User already Exists. Try Signing in!");
        }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            log.info("User Saved");
            authRepository.save(user);



        return user;
    }

    @Override
    public LoginResponse login(User inputUser) {
        User user = authRepository.findByEmail(inputUser.getEmail()).orElseThrow(UserNotFoundException::new);
        if (passwordEncoder.matches(inputUser.getPassword(), user.getPassword())) {
            String generatedToken = jwtService.generateToken(user);
            return AuthMapper.userToLoginResponse(user, true, "Success", generatedToken);
        } else {
            log.error("Invalid Credentials");
            throw new InvalidCredentialsException("Invalid Credentials");
        }
    }

    @Override
    public Response changePass(ChangePassRequest request, String token) {
        Response confirmation = new Response("");
        String email = jwtService.extractEmail(token);
        User user = authRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            authRepository.save(user);
            
            confirmation.setMessage("Password has been Changed");
            log.info("Password has been Changed");
        } else {
            log.error("Old Password is Incorrect");
           throw new InvalidCredentialsException("Old Password is Incorrect");
        }
        return confirmation;
    }

}
