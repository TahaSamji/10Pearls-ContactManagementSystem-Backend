package com.project.contactmanagementsystem.authTests;
import com.project.contactmanagementsystem.auth.models.User;
import com.project.contactmanagementsystem.auth.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private User testUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("testuser@example.com");
    }

    @Test
    void generateToken_Success() {

        String token = jwtService.generateToken(testUser);


        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void extractEmail_Success() {

        String token = jwtService.generateToken(testUser);


        String extractedEmail = jwtService.extractEmail(token);


        assertEquals(testUser.getEmail(), extractedEmail);
    }

    @Test
    void validateToken_Success() {

        String token = jwtService.generateToken(testUser);

        boolean isValid = jwtService.validateToken(token, testUser);

        assertTrue(isValid);
    }


}
