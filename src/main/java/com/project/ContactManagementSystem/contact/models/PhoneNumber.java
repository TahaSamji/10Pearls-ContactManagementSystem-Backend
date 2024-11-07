package com.project.ContactManagementSystem.contact.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "phoneNumber")
public class PhoneNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private long workNumber;
    private long homeNumber;
    private long personalNumber;

    public PhoneNumber(long workNumber, long homeNumber, long personalNumber) {
        this.workNumber = workNumber;
        this.homeNumber = homeNumber;
        this.personalNumber = personalNumber;
    }
}
