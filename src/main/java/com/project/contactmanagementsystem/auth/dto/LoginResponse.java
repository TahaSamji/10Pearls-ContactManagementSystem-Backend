package com.project.contactmanagementsystem.auth.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private String email;
    private String message;
    private boolean success;
    private String token;
    private long userId;

    public LoginResponse(long userId,String username, boolean success, String message, String token) {
        this.userId = userId;
        this.email = username;
        this.success = success;
        this.message = message;
        this.token = token;
    }


    public LoginResponse(boolean success, String message) {

        this.success = success;
        this.message = message;
    }





}
