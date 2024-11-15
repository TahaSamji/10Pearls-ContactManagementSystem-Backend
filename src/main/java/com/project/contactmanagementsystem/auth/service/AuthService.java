package com.project.contactmanagementsystem.auth.service;

import com.project.contactmanagementsystem.auth.dto.ChangePassRequest;
import com.project.contactmanagementsystem.auth.dto.LoginResponse;
import com.project.contactmanagementsystem.auth.models.User;
import com.project.contactmanagementsystem.dto.Response;

public interface AuthService {

    public User saveUser(User user);

    public LoginResponse login(User inputUser);

    public Response changePass(ChangePassRequest request, String token);

}
