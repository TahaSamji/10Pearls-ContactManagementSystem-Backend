package com.project.ContactManagementSystem.contact.controller;

import com.project.ContactManagementSystem.auth.models.User;
import com.project.ContactManagementSystem.contact.dto.Contactdto;
import com.project.ContactManagementSystem.contact.models.ContactProfile;
import com.project.ContactManagementSystem.contact.service.ContactService;
import com.project.ContactManagementSystem.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactController {

    @Autowired
    ContactService contactService;
    @PostMapping("/addcontact")
    public String AddContact(@RequestBody ContactProfile contact,@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        contactService.SaveContact(contact,token);
        return "Contact Created Successfully";
    }
    @GetMapping("/showcontacts")
    public List<Contactdto> ShowContacts(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
       List<Contactdto> contacts  = contactService.ViewMyContacts(token);
        return contacts;
    }
    @PostMapping("/updatecontact")
    public ResponseEntity<ContactProfile> UpdateContact(@RequestBody ContactProfile contact,@RequestParam(name ="contactId") long contactId) {
        ContactProfile UpdatedContact = contactService.UpdateContact(contact,contactId);
        return new ResponseEntity<>(UpdatedContact, HttpStatus.OK);
    }
    @DeleteMapping("/delectcontact")
    public ResponseEntity<ContactProfile> DeleteContact(@RequestParam(name ="contactId") long contactId) {
        ContactProfile deletedProduct = contactService.DeleteContact(contactId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }
    @GetMapping("/searchcontacts")
    public ResponseEntity<List<ContactProfile>> SearchContact(@RequestParam(name ="value") String value,@RequestParam(name ="userId") long userId) {
        List<ContactProfile> MySearch = contactService.SearchContact(value,userId);
        return new ResponseEntity<>(MySearch, HttpStatus.OK);
    }

}
