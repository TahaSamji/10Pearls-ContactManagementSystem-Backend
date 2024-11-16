package com.project.contactmanagementsystem.auth.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private String email;
    private String message;
    private boolean success;
    private String token;
    private String name;
    private long userId;

    public LoginResponse(long userId,String name,String username, boolean success, String message, String token) {
        this.userId = userId;
        this.email = username;
        this.success = success;
        this.message = message;
        this.name= name;
        this.token = token;
    }




}
