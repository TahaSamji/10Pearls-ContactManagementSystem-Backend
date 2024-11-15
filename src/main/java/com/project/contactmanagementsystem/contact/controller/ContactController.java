package com.project.contactmanagementsystem.contact.controller;

import com.project.contactmanagementsystem.contact.dto.Contactdto;
import com.project.contactmanagementsystem.contact.models.ContactProfile;
import com.project.contactmanagementsystem.contact.service.ContactService;
import com.project.contactmanagementsystem.dto.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ContactController {



    private  ContactService contactService;

    public ContactController(ContactService contactService){
        this.contactService = contactService;
    }


    @PostMapping("/addcontact")
    public ResponseEntity<Response> addContact(@RequestBody Contactdto contact,@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        contactService.saveContact(contact,token);
        Response confirmation = new Response("Contact Has been Added");
        return new ResponseEntity<>(confirmation, HttpStatus.OK);
    }

    @GetMapping("/showcontacts")
    public ResponseEntity<List<ContactProfile>> showContacts(@RequestParam(name ="userId",defaultValue = "1") long userId,@RequestParam(name ="value",defaultValue = "") String value, @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "2") int pageSize) {
        List<ContactProfile> contactList =  contactService.viewMyContacts(userId,value,pageNo,pageSize);
        return new ResponseEntity<>(contactList, HttpStatus.OK);
    }
    @PostMapping("/updatecontact")
    public ResponseEntity<ContactProfile> updateContact(@RequestBody Contactdto contact,@RequestParam(name ="contactId") long contactId) {

        ContactProfile updatedContact = contactService.updateContact(contact,contactId);
        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }
    @PostMapping("/delectcontact")
    public ResponseEntity<Response> deleteContact(@RequestParam(name ="contactId") long contactId) {
        contactService.deleteContact(contactId);
        Response confirmation = new Response("Contact has been Deleted");
        return new ResponseEntity<>(confirmation, HttpStatus.OK);
    }

    @GetMapping("/contactexport")
    public ResponseEntity<byte[]> export(@RequestParam(name ="userId") long userId) {
        return  contactService.exportContact(userId);
    }
    @PostMapping("/contactimport")
    public ResponseEntity<Response> contactimport(@RequestBody MultipartFile file, @RequestParam("userId") long userId) throws IOException {

        return  contactService.contactImport(file,userId);
    }

}
