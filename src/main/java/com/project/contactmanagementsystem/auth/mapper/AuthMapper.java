package com.project.contactmanagementsystem.auth.mapper;

import com.project.contactmanagementsystem.auth.customexceptions.InvalidCredentialsException;
import com.project.contactmanagementsystem.auth.dto.LoginRequest;
import com.project.contactmanagementsystem.auth.dto.LoginResponse;
import com.project.contactmanagementsystem.auth.models.User;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class AuthMapper {

    private AuthMapper() {
        throw new UnsupportedOperationException("AuthMapper is a utility class and cannot be instantiated");
    }

    public static User loginRequestToUser(LoginRequest loginRequest) {

        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            log.error("Email or Password Should not be Empty");
            throw new InvalidCredentialsException("Email or Password Should not be Empty");
        }
        User user = new User();
        user.setEmail(loginRequest.getEmail());
        user.setPassword(loginRequest.getPassword());
        return user;

    }
}
