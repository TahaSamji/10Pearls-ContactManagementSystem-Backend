package com.project.contactmanagementsystem.contact.service;


import com.project.contactmanagementsystem.contact.dto.Contactdto;
import com.project.contactmanagementsystem.contact.models.ContactProfile;
import com.project.contactmanagementsystem.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ContactService {
    ContactProfile saveContact(Contactdto contact,String token);
    List<ContactProfile> viewMyContacts(long userId, String value, int pageNo, int pageSize);
    ContactProfile updateContact(Contactdto contact,Long contactId);
    ContactProfile deleteContact(long contactId);
    ResponseEntity<byte[]> exportContact(long userId);
    ResponseEntity<Response> contactImport(MultipartFile file, long userId) throws IOException;
}
