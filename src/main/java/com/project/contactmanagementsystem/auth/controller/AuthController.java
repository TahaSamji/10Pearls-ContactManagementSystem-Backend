package com.project.contactmanagementsystem.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.project.contactmanagementsystem.auth.dto.ChangePassRequest;
import com.project.contactmanagementsystem.auth.dto.LoginRequest;
import com.project.contactmanagementsystem.auth.dto.LoginResponse;
import com.project.contactmanagementsystem.auth.mapper.AuthMapper;
import com.project.contactmanagementsystem.auth.models.User;
import com.project.contactmanagementsystem.auth.service.AuthService;
import com.project.contactmanagementsystem.dto.Response;

@RestController()
public class AuthController {

    private final AuthService authService;
    public  AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signup")
    public  ResponseEntity<Response> createUser(@RequestBody  @Valid User user) {
        authService.saveUser(user);
        Response confirmation = new Response("Your account has been created");
        return new ResponseEntity<>(confirmation, HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(AuthMapper.loginRequestToUser(loginRequest));
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/ChangePassword")
    public ResponseEntity<Response> changePassword(@RequestBody  @Valid  ChangePassRequest changeRequest, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Response confirmation = authService.changePass(changeRequest, token);
        return new ResponseEntity<>(confirmation, HttpStatus.OK);
    }




}
