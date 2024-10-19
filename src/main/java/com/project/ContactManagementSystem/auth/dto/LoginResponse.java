package com.project.ContactManagementSystem.auth.dto;

public class LoginResponse {

    private String username;
    private String message;
    private boolean success;
    private String token;

    public LoginResponse(String username, boolean success, String message, String token) {
        this.username = username;
        this.success = success;
        this.message = message;
        this.token = token;
    }

    public LoginResponse(boolean success, String message) {

        this.success = success;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
