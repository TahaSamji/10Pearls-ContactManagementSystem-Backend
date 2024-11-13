package com.project.ContactManagementSystem.auth.mapper;

import com.project.ContactManagementSystem.auth.customexceptions.InvalidCredentialsException;
import com.project.ContactManagementSystem.auth.dto.LoginRequest;
import com.project.ContactManagementSystem.auth.dto.LoginResponse;
import com.project.ContactManagementSystem.auth.models.User;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class AuthMapper {

    public static LoginResponse UserToLoginResponse(User user, boolean success, String message, String token) {
        return new LoginResponse(user.getId(),user.getUsername(), success, message, token);
    }




    public static User LoginRequestToUser(LoginRequest loginRequest) {

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
