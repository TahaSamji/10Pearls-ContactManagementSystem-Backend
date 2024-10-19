package com.project.ContactManagementSystem.auth.mapper;

import com.project.ContactManagementSystem.auth.dto.LoginRequest;
import com.project.ContactManagementSystem.auth.dto.LoginResponse;
import com.project.ContactManagementSystem.auth.models.User;

public class AuthMapper {


   
    public  static LoginResponse UserToLoginResponse(User user,boolean success,String message,String token) {
        if (success == true){ 
            return new LoginResponse(user.getUsername(),success,message,token);
        }else{
            return new LoginResponse(success,message);
        }
        
    }

    public static User LoginRequestToUser(LoginRequest loginRequest) {
        
        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            throw new RuntimeException("Email or password is missing");
        }

        User user = new User();
        user.setEmail(loginRequest.getEmail());
        user.setPassword(loginRequest.getPassword());
        return user;

    }
}