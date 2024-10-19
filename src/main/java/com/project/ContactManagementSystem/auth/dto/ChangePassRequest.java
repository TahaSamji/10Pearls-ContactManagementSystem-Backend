package com.project.ContactManagementSystem.auth.dto;

public class ChangePassRequest {

    private String OldPassword;
    private String NewPassword;

    public ChangePassRequest(String OldPassword, String NewPassword) {
        this.OldPassword = OldPassword;
        this.NewPassword = NewPassword;
    }

    public String getOldPassword() {
        return OldPassword;
    }

    public void setOldPassword(String oldPassword) {
        OldPassword = oldPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

}
