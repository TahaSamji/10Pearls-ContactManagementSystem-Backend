package com.project.contactmanagementsystem.dto;

import lombok.Data;

@Data
public class ErrorResponse {

    private int statusCode;
    private String message;

    public ErrorResponse(int statusCode,String message)
    {
        this.message = message;
        this.statusCode = statusCode;
    }



    
}