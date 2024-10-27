package com.project.ContactManagementSystem.contact.service;

import com.project.ContactManagementSystem.auth.customexceptions.UserNotFoundException;
import com.project.ContactManagementSystem.auth.models.User;
import com.project.ContactManagementSystem.auth.repositories.AuthRepository;
import com.project.ContactManagementSystem.auth.service.JwtService;
import com.project.ContactManagementSystem.contact.customexceptions.ContactNotFoundException;
import com.project.ContactManagementSystem.contact.dto.Contactdto;
import com.project.ContactManagementSystem.contact.models.ContactProfile;
import com.project.ContactManagementSystem.contact.repositories.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
@Slf4j
@Service
public class ContactServiceImpl  implements ContactService{

    @Autowired
    ContactRepository contactRepository;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    JwtService jwtService;
    @Override
    public ContactProfile SaveContact(ContactProfile contact,String token) {
       String email  = jwtService.extractUserName(token);
       User user = authRepository.findByEmail(email).orElseThrow( ()->new UserNotFoundException("User Not Found"));
       contact.setUser(user);

       return contactRepository.save(contact);
    }

    @Override
    public List<Contactdto> ViewMyContacts(String token) {
        String email  = jwtService.extractUserName(token);
        User user = authRepository.findByEmail(email).orElseThrow( ()->new UserNotFoundException("User Not Found"));

        List<Contactdto>  contacts = contactRepository.findByUser(user);

        return contacts;
    }

    @Override
    public ContactProfile UpdateContact(ContactProfile contact,Long ContactId) {
        ContactProfile existingContact = contactRepository.findById(ContactId).orElseThrow(() -> new ContactNotFoundException("Contact Not Found"));
        existingContact.setFirstName(contact.getFirstName());
        existingContact.setLastName((contact.getLastName()));
        existingContact.setTitle(contact.getTitle());
        existingContact.setPhoneNumber(contact.getPhoneNumber());
        existingContact.setEmailAddress(contact.getEmailAddress());
        contactRepository.save(existingContact);


        return existingContact;
    }
    public ContactProfile DeleteContact(long ContactId) {

        ContactProfile existingContact = contactRepository.findById(ContactId).orElseThrow(() -> new ContactNotFoundException("Contact Not Found"));

        contactRepository.deleteById(ContactId);
        return existingContact;
    }

    @Override
    public List<ContactProfile> SearchContact(String value,long userId) {
    String  LowerCaseValue = value.toLowerCase(Locale.ROOT);
        log.info(LowerCaseValue);
       List<ContactProfile> MySearch = contactRepository.search(LowerCaseValue,userId);
        return MySearch;
    }

}
