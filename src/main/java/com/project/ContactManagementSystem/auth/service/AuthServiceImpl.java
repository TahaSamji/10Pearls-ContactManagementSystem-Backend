package com.project.ContactManagementSystem.auth.service;

import com.project.ContactManagementSystem.auth.customexceptions.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.ContactManagementSystem.auth.customexceptions.InvalidCredentialsException;
import com.project.ContactManagementSystem.auth.customexceptions.UserNotFoundException;
import com.project.ContactManagementSystem.auth.dto.ChangePassRequest;
import com.project.ContactManagementSystem.auth.dto.LoginResponse;
import com.project.ContactManagementSystem.auth.mapper.AuthMapper;
import com.project.ContactManagementSystem.auth.models.User;
import com.project.ContactManagementSystem.auth.repositories.AuthRepository;
import com.project.ContactManagementSystem.dto.Response;

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
        User user = authRepository.findByEmail(inputUser.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (passwordEncoder.matches(inputUser.getPassword(), user.getPassword())) {
            String GeneratedToken = jwtService.generateToken(user);
            return AuthMapper.UserToLoginResponse(user, true, "Success", GeneratedToken);
        } else {
            log.error("Invalid Credentials");
            throw new InvalidCredentialsException("Invalid Credentials");
        }
    }

    @Override
    public Response ChangePass(ChangePassRequest request, String token) {
        Response confirmation = new Response("");
        String email = jwtService.extractUserName(token);
        User user = authRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
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
