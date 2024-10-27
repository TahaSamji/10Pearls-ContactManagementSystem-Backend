package com.project.ContactManagementSystem.contact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Contactdto {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private long profileId;
    private long phoneNumber;
    private String title;

}
