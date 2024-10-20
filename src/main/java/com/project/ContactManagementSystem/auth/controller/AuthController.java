package com.project.ContactManagementSystem.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.project.ContactManagementSystem.auth.dto.ChangePassRequest;
import com.project.ContactManagementSystem.auth.dto.LoginRequest;
import com.project.ContactManagementSystem.auth.dto.LoginResponse;
import com.project.ContactManagementSystem.auth.mapper.AuthMapper;
import com.project.ContactManagementSystem.auth.models.User;
import com.project.ContactManagementSystem.auth.service.AuthService;
import com.project.ContactManagementSystem.dto.Response;

@RestController()
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public String CreateUser(@RequestBody User user) {
        authService.saveUser(user);
        return "User Created Successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest login_request) {
        LoginResponse loginResponse = authService.login(AuthMapper.LoginRequestToUser(login_request));

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/ChangePassword")
    public ResponseEntity<Response> ChangePassword(@RequestBody ChangePassRequest change_request, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Response confirmation = authService.ChangePass(change_request, token);
        return new ResponseEntity<>(confirmation, HttpStatus.OK);
    }

    @GetMapping("/Test")
    public String Hello(@RequestHeader("Authorization") String authorizationHeader) {

        return "Test";
    }

}
