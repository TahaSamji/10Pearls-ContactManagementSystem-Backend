package com.project.ContactManagementSystem.contact.models;

import com.project.ContactManagementSystem.auth.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contactProfiles")
public class ContactProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;
    private String firstName;
    private String lastName;
    private String title;
    private String emailAddress;
    private Long phoneNumber;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

}
