package com.project.ContactManagementSystem.contact.service;


import com.project.ContactManagementSystem.auth.models.User;
import com.project.ContactManagementSystem.contact.dto.Contactdto;
import com.project.ContactManagementSystem.contact.models.ContactProfile;

import java.util.List;

public interface ContactService {
    ContactProfile SaveContact(ContactProfile contact,String token);

    List<Contactdto> ViewMyContacts(String token);
    ContactProfile UpdateContact(ContactProfile contact,Long ContactId);

    ContactProfile DeleteContact(long contactId);
    List<ContactProfile> SearchContact(String value,long userId);
}
