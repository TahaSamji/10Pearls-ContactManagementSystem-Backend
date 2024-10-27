package com.project.ContactManagementSystem.contact.customexceptions;

public class ContactNotFoundException extends RuntimeException{
    public ContactNotFoundException(String message) {
        super(message);
    }
}
