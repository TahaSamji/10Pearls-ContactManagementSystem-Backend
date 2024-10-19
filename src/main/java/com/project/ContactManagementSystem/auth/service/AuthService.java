package com.project.ContactManagementSystem.auth.service;


import com.project.ContactManagementSystem.auth.dto.LoginResponse;
import com.project.ContactManagementSystem.auth.models.User;


public interface AuthService {
    public User saveUser(User user);
    public LoginResponse login(User inputUser);
    

}