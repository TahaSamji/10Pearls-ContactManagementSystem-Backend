package com.project.ContactManagementSystem.auth.service;

import com.project.ContactManagementSystem.auth.dto.ChangePassRequest;
import com.project.ContactManagementSystem.auth.dto.LoginResponse;
import com.project.ContactManagementSystem.auth.models.User;
import com.project.ContactManagementSystem.dto.Response;

public interface AuthService {

    public User saveUser(User user);

    public LoginResponse login(User inputUser);

    public Response ChangePass(ChangePassRequest request, String token);

}
