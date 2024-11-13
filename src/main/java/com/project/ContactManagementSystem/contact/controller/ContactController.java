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
    public String AddContact(@RequestBody Contactdto contact,@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        contactService.SaveContact(contact,token);
        return "Contact Created Successfully";
    }

    @GetMapping("/showcontacts")
    public List<ContactProfile> ShowContacts(@RequestParam(name ="userId",defaultValue = "1") long userId,@RequestParam(name ="value",defaultValue = "") String value, @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "2") int pageSize) {
        return    contactService.ViewMyContacts(userId,value,pageNo,pageSize);
    }
    @PostMapping("/updatecontact")
    public ResponseEntity<ContactProfile> UpdateContact(@RequestBody Contactdto contact,@RequestParam(name ="contactId") long contactId) {

        ContactProfile UpdatedContact = contactService.UpdateContact(contact,contactId);
        return new ResponseEntity<>(UpdatedContact, HttpStatus.OK);
    }
    @PostMapping("/delectcontact")
    public ResponseEntity<ContactProfile> DeleteContact(@RequestParam(name ="contactId") long contactId) {
        ContactProfile deletedProduct = contactService.DeleteContact(contactId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }
    @GetMapping("/searchcontacts")
    public ResponseEntity<List<ContactProfile>> SearchContact(@RequestParam(name ="value") String value,@RequestParam(name ="userId") long userId,@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "2") int pageSize) {
        List<ContactProfile> MySearch = contactService.SearchContact(value,userId,pageNo,pageSize);
        return new ResponseEntity<>(MySearch, HttpStatus.OK);
    }
    @GetMapping("/contactexport")
    public ResponseEntity<byte[]> ExportContact(@RequestParam(name ="userId") long userId) {
        return  contactService.ExportContact(userId);
    }
    @GetMapping("/contactimport")
    public ResponseEntity<Response> ImportContact(@RequestParam("file") MultipartFile file, @RequestParam("userId") long userId) throws IOException {

        return  contactService.importContact(file,userId);
    }

}
