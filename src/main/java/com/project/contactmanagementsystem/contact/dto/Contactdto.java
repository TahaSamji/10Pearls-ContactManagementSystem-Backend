package com.project.contactmanagementsystem.contact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Contactdto {
    private String firstName;
    private String lastName;
    private String workEmailAddress;
    private String personalEmailAddress;
    private String otherEmailAddress;
    private long profileId;
    private long homePhoneNumber;
    private long personalPhoneNumber;
    private long workPhoneNumber;
    private String title;

}
