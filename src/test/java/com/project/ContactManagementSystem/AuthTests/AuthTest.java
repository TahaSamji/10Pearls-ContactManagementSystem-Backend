package com.project.ContactManagementSystem.AuthTests;

import com.project.ContactManagementSystem.auth.customexceptions.InvalidCredentialsException;
import com.project.ContactManagementSystem.auth.customexceptions.UserAlreadyExistsException;
import com.project.ContactManagementSystem.auth.dto.ChangePassRequest;
import com.project.ContactManagementSystem.auth.dto.LoginResponse;
import com.project.ContactManagementSystem.auth.models.User;
import com.project.ContactManagementSystem.auth.repositories.AuthRepository;
import com.project.ContactManagementSystem.auth.service.AuthService;
import com.project.ContactManagementSystem.auth.service.AuthServiceImpl;
import com.project.ContactManagementSystem.auth.service.JwtService;
import com.project.ContactManagementSystem.contact.service.ContactServiceImpl;
import com.project.ContactManagementSystem.dto.Response;
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
public class AuthTest {
    @Mock
    private AuthRepository authRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    PasswordEncoder passwordEncoder ;


    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void createUserTest_success(){
        User user = new User(1L, "test", "test123", "test@gmail.com");

        User result = authService.saveUser(user);
        assertNotEquals(result,null);

        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user, result);
    }
    @Test
    public void createUserTest_Failure() {

        User user = new User(1L, "test", "test123", "test@gmail.com");

        when(authRepository.findByemail(user.getEmail())).thenReturn(user);

        assertThrows(UserAlreadyExistsException.class, () -> {
            authService.saveUser(user);
        });
    }
    @Test
    public void LoginTest_Success() {
        String token = "jwttoken";
        User userInput = new User(1L, "Test", "hello", "Test@gmail.com");
        User user = new User(2L, "Test", "hello", "Test@gmail.com");
        when(authRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userInput.getPassword(),user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(token);
        LoginResponse loginResponse =   authService.login(userInput);
       log.info(loginResponse.getToken());
  assertEquals(loginResponse.getUsername(),user.getUsername());
        assertEquals(token,loginResponse.getToken());

    }
    @Test
    public void LoginTest_Failure() {

        User userInput = new User(1L, "Test", "hello", "Test@gmail.com");
        User user = new User(2L, "Test", "hello", "Test@gmail.com");

        when(authRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userInput.getPassword(),user.getPassword())).thenReturn(false);
        assertThrows(InvalidCredentialsException.class, () -> {
            authService.login(userInput);
        });
    }
    @Test
    public void ChangePasswordTest_Success() {


        User user = new User(2L, "Test", "hello", "Test@gmail.com");
        ChangePassRequest request = new ChangePassRequest(user.getPassword(),"hello123");
        when(jwtService.extractUserName(" ")).thenReturn(user.getEmail());
        when(authRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).thenReturn(true);
        Response result = authService.ChangePass(request, " ");

        assertEquals("Password has been Changed", result.getMessage());
    }
    @Test
    public void ChangePasswordTest_Failure() {

        User user = new User(2L, "Test", "hello", "Test@gmail.com");
        ChangePassRequest request = new ChangePassRequest(user.getPassword(),"hello123");
        when(jwtService.extractUserName(" ")).thenReturn(user.getEmail());
        when(authRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).thenReturn(false);


        assertThrows(InvalidCredentialsException.class, () -> {
            authService.ChangePass(request," ");
        });
    }

}
