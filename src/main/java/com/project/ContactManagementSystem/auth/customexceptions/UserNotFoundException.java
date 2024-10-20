package com.project.ContactManagementSystem.auth.customexceptions;

public class UserNotFoundException extends RuntimeException{
    
    public UserNotFoundException(String message) {
        super(message);
    }

}
