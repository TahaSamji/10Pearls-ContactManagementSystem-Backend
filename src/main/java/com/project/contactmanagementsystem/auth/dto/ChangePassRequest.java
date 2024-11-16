package com.project.contactmanagementsystem.auth.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePassRequest {

    private String oldPassword;
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String newPassword;

    public ChangePassRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }



}
