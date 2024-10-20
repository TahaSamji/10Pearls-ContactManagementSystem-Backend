package com.project.ContactManagementSystem.auth.customexceptions;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
   
}
