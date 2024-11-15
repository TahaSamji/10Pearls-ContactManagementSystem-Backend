package com.project.contactmanagementsystem.auth.customexceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("User Not found");
    }

}
