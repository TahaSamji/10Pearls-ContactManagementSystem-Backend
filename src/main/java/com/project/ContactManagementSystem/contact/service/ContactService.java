package com.project.ContactManagementSystem.contact.service;


import com.project.ContactManagementSystem.auth.models.User;
import com.project.ContactManagementSystem.contact.dto.Contactdto;
import com.project.ContactManagementSystem.contact.models.ContactProfile;
import com.project.ContactManagementSystem.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ContactService {
    ContactProfile SaveContact(Contactdto contact,String token);
    List<ContactProfile> ViewMyContacts(long userId, String value, int pageNo, int pageSize);
    ContactProfile UpdateContact(Contactdto contact,Long ContactId);
    ContactProfile DeleteContact(long contactId);
    List<ContactProfile> SearchContact(String value,long userId,int pageNo, int pageSize);
    ResponseEntity<byte[]> ExportContact(long userId);
    ResponseEntity<Response> importContact(MultipartFile file, long userId) throws IOException;
}
