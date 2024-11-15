package com.project.contactmanagementsystem.auth.dto;

import lombok.Data;

@Data
public class ChangePassRequest {

    private String oldPassword;
    private String newPassword;

    public ChangePassRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }



}
