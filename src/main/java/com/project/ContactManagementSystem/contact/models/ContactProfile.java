package com.project.ContactManagementSystem.contact.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.ContactManagementSystem.auth.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor

@Entity
@Table(name = "contactProfiles")
public class ContactProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;
    private String firstName;
    private String lastName;
    private String title;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "phoneNumbers")
    private PhoneNumber phoneNumbers;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "emailAddresses")
    private EmailAddress emailAddresses;
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;

    public ContactProfile(String firstName, String lastName, String title, PhoneNumber phoneNumbers, EmailAddress emailAddresses, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.phoneNumbers = phoneNumbers;
        this.emailAddresses = emailAddresses;
        this.user = user;
    }

    public ContactProfile(long profileId, String firstName, String lastName, String title,User user) {
        this.profileId  = profileId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.user= user;
    }
}
