package com.project.contactmanagementsystem.contact.models;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "emailAddress")
public class EmailAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String workEmail;
    private String personalEmail;
    private String otherEmail;


    public EmailAddress(String workEmail, String personalEmail, String otherEmail) {
        this.workEmail = workEmail;
        this.personalEmail = personalEmail;
        this.otherEmail = otherEmail;
    }
}
