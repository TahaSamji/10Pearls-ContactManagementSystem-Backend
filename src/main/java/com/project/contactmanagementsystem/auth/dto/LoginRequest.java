package com.project.contactmanagementsystem.auth.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class LoginRequest {
    @Email
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }


}
