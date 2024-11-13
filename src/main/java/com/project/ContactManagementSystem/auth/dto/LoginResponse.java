package com.project.ContactManagementSystem.auth.dto;

public class LoginResponse {

    private String username;
    private String message;
    private boolean success;
    private String token;
    private long userId;

    public LoginResponse(long userId,String username, boolean success, String message, String token) {
        this.userId = userId;
        this.username = username;
        this.success = success;
        this.message = message;
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
