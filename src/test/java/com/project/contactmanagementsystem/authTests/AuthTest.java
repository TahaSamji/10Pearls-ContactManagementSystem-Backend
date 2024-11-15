package com.project.contactmanagementsystem.authTests;

import com.project.contactmanagementsystem.auth.customexceptions.InvalidCredentialsException;
import com.project.contactmanagementsystem.auth.customexceptions.UserAlreadyExistsException;
import com.project.contactmanagementsystem.auth.dto.ChangePassRequest;
import com.project.contactmanagementsystem.auth.dto.LoginResponse;
import com.project.contactmanagementsystem.auth.models.User;
import com.project.contactmanagementsystem.auth.repositories.AuthRepository;

import com.project.contactmanagementsystem.auth.service.AuthServiceImpl;
import com.project.contactmanagementsystem.auth.service.JwtService;
import com.project.contactmanagementsystem.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;


import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
 class AuthTest {
    @Mock
    private AuthRepository authRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    PasswordEncoder passwordEncoder ;


    @InjectMocks
    private AuthServiceImpl authService;

    @Test
     void createUserTest_success(){
        User user = new User(1L, "test", "test123", "test@gmail.com");
        when(authRepository.findByemail(user.getEmail())).thenReturn(null);
        User result = authService.saveUser(user);
        assertNotEquals(null,result);

        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user, result);
    }
    @Test
     void createUserTest_Failure() {

        User user = new User(1L, "test", "test123", "test@gmail.com");

        when(authRepository.findByemail(user.getEmail())).thenReturn(user);

        assertThrows(UserAlreadyExistsException.class, () -> {
            authService.saveUser(user);
        });
    }
    @Test
     void LoginTest_Success() {
        String token = "jwttoken";
        User userInput = new User(1L, "Test", "hello", "Test@gmail.com");
        User user = new User(2L, "Test", "hello", "Test@gmail.com");
        when(authRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userInput.getPassword(),user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(token);
        LoginResponse loginResponse =   authService.login(userInput);

        assertEquals(loginResponse.getEmail(),user.getUsername());
        assertEquals(token,loginResponse.getToken());

    }
    @Test
     void LoginTest_Failure() {

        User userInput = new User(1L, "Test", "hello", "Test@gmail.com");
        User user = new User(2L, "Test", "hello", "Test@gmail.com");

        when(authRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userInput.getPassword(),user.getPassword())).thenReturn(false);
        assertThrows(InvalidCredentialsException.class, () -> {
            authService.login(userInput);
        });
    }
    @Test
     void ChangePasswordTest_Success() {


        User user = new User(2L, "Test", "hello", "Test@gmail.com");
        ChangePassRequest request = new ChangePassRequest(user.getPassword(),"hello123");
        when(jwtService.extractEmail(" ")).thenReturn(user.getEmail());
        when(authRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).thenReturn(true);
        Response result = authService.changePass(request, " ");

        assertEquals("Password has been Changed", result.getMessage());
    }
    @Test
     void ChangePasswordTest_Failure() {
        String token = "token";
        User user = new User(2L, "Test", "hello", "Test@gmail.com");
        ChangePassRequest request = new ChangePassRequest(user.getPassword(),"hello123");
        when(jwtService.extractEmail(token)).thenReturn(user.getEmail());
        when(authRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).thenReturn(false);


        assertThrows(InvalidCredentialsException.class, () -> {
            authService.changePass(request,token);

        });
    }

}
