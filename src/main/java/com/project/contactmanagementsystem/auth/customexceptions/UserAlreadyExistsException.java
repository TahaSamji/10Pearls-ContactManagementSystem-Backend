package com.project.contactmanagementsystem.auth.customexceptions;

public class UserAlreadyExistsException extends RuntimeException {


        public UserAlreadyExistsException(String message) {

            super(message);
        }


}
