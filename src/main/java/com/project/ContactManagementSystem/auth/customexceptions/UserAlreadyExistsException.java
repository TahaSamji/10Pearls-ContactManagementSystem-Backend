package com.project.ContactManagementSystem.auth.customexceptions;

public class UserAlreadyExistsException extends RuntimeException {


        public UserAlreadyExistsException(String message) {

            super(message);
        }


}
