package com.project.contactmanagementsystem.auth.customexceptions;

public class SystemErrorException  extends RuntimeException{
    public SystemErrorException(String message) {
        super(message);
    }
}
