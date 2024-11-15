package com.project.contactmanagementsystem.authTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.contactmanagementsystem.auth.controller.AuthController;
import com.project.contactmanagementsystem.auth.customexceptions.InvalidCredentialsException;
import com.project.contactmanagementsystem.auth.customexceptions.UserAlreadyExistsException;
import com.project.contactmanagementsystem.auth.dto.ChangePassRequest;
import com.project.contactmanagementsystem.auth.dto.LoginRequest;
import com.project.contactmanagementsystem.auth.dto.LoginResponse;
import com.project.contactmanagementsystem.auth.models.User;
import com.project.contactmanagementsystem.auth.service.AuthService;
import com.project.contactmanagementsystem.dto.ErrorResponse;
import com.project.contactmanagementsystem.dto.Response;
import com.project.contactmanagementsystem.exceptionhandler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
 class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    private User user = new User(1L, "testUser", "password123", "test@example.com");
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Manually add the GlobalExceptionHandler
                .build();
    }
    @Test
    void createUser_Success() throws Exception {
        String userJson = objectMapper.writeValueAsString(user);
        Response response = new Response("Your account has been created");
        String responseJson = objectMapper.writeValueAsString(response);

        when(authService.saveUser(any(User.class))).thenReturn(user);


        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseJson));
    }

    @Test
    void createUser_Failure() throws Exception {

        ErrorResponse response = new ErrorResponse(401,"User Already Exists");
        String responseJson = objectMapper.writeValueAsString(response);

        when(authService.saveUser(any(User.class))).thenThrow(new UserAlreadyExistsException("User Already Exists"));
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/signup").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())

                .andExpect(content().string(responseJson));

    }
    @Test
    void login_Success() throws Exception {
        String token = "jwttoken";
        LoginResponse loginResponse = new LoginResponse(user.getId(),user.getEmail(),true,"Success", token);
        LoginRequest loginRequest = new LoginRequest(user.getEmail(),user.getPassword());
        when(authService.login(any(User.class))).thenReturn(loginResponse);

        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.token").value(token))
                .andDo(print());


    }
    @Test
    void login_Failure() throws Exception {

        ErrorResponse response = new ErrorResponse(401,"User Already Exists");
        String responseJson = objectMapper.writeValueAsString(response);

        LoginRequest loginRequest = new LoginRequest(user.getEmail(),user.getPassword());
        when(authService.login(any(User.class))).thenThrow(new UserAlreadyExistsException("User Already Exists"));
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(responseJson))
                .andDo(print());


    }


    @Test
    void changePassword_Success() throws Exception {
        String token = "Bearer jwttoken";
        Response confirmation = new Response("Password has been Changed");

        ChangePassRequest request = new ChangePassRequest("Old", "New");
        when(authService.changePass(request, "jwttoken")).thenReturn(confirmation);
        String expectedResponseJson = objectMapper.writeValueAsString(confirmation);

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/ChangePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponseJson))
                .andDo(print());
    }
    @Test
    void changePassword_Failure() throws Exception {
        String token = "Bearer jwttoken";

        ErrorResponse response = new ErrorResponse(401,"Invalid Credentials");

        ChangePassRequest request = new ChangePassRequest("Old", "New");
        when(authService.changePass(request, "jwttoken")).thenThrow(new InvalidCredentialsException("Invalid Credentials"));
        String expectedResponseJson = objectMapper.writeValueAsString(response);

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/ChangePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(requestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(expectedResponseJson))
                .andDo(print());
    }


}
