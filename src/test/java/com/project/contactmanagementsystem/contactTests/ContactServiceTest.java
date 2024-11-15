package com.project.contactmanagementsystem.contactTests;

import com.project.contactmanagementsystem.auth.customexceptions.UserNotFoundException;
import com.project.contactmanagementsystem.auth.models.User;
import com.project.contactmanagementsystem.auth.repositories.AuthRepository;
import com.project.contactmanagementsystem.auth.service.JwtService;
import com.project.contactmanagementsystem.contact.customexceptions.ContactNotFoundException;
import com.project.contactmanagementsystem.contact.dto.Contactdto;
import com.project.contactmanagementsystem.contact.models.ContactProfile;
import com.project.contactmanagementsystem.contact.models.EmailAddress;
import com.project.contactmanagementsystem.contact.models.PhoneNumber;
import com.project.contactmanagementsystem.contact.repositories.ContactRepository;
import com.project.contactmanagementsystem.contact.service.ContactServiceImpl;
import com.project.contactmanagementsystem.dto.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private AuthRepository authRepository;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ContactServiceImpl contactServiceImpl;


    @Test
     void getAllContacts_success() {
        User user = new User(1L, "Taha", "hello", "taha@gmail.com");

        ContactProfile contact1 = new ContactProfile(1L, "John", "Doe", "john.doe@example.com", user);
        ContactProfile contact2 = new ContactProfile(2L, "Jane", "Smith", "jane.smith@example.com", user);
        List<ContactProfile> contactList = Arrays.asList(contact1, contact2);
        Pageable pageable = PageRequest.of(0, 2);

        when(authRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(contactRepository.findAllByUser(user, pageable)).thenReturn(contactList);

        List<ContactProfile> result = contactServiceImpl.viewMyContacts(1L, "", 0, 2);
        assertEquals(2, result.size());
        assertEquals("John", result.getFirst().getFirstName());
    }
    @Test
     void getAllContacts_Failure() {


        when(authRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            contactServiceImpl.viewMyContacts(1L, "", 0, 2);
        });


    }
    @Test
     void SearchContacts_Success() {
        User user = new User(1L, "Taha", "hello", "taha@gmail.com");

        ContactProfile contact2 = new ContactProfile(2L, "Jane", "Smith", "jane.smith@example.com", user);
        List<ContactProfile> expectedResult = List.of(contact2);
        Pageable pageable = PageRequest.of(0, 2);

        when(authRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(contactRepository.search("Jane".toLowerCase(),user.getId(),pageable)).thenReturn(expectedResult);


        List<ContactProfile> result = contactServiceImpl.viewMyContacts(1L, "Jane", 0, 2);
        assertEquals(expectedResult, result);
        assertEquals("Jane", result.getFirst().getFirstName());
        verify(contactRepository, never()).findAllByUser(any(User.class), any(Pageable.class));

    }

    @Test
     void CreateContacts_Success() {
        User user = new User(1L, "testUser", "test123", "test@gmail.com");
        Contactdto contact = new Contactdto("testUser","user","test@example.com","test1@example.com","test2@example.com",1,0,0,0,"test123");
        EmailAddress emailAddress = new EmailAddress(contact.getWorkEmailAddress(),contact.getPersonalEmailAddress(),contact.getOtherEmailAddress());
        PhoneNumber phoneNumber  = new PhoneNumber(contact.getWorkPhoneNumber(),contact.getHomePhoneNumber(),contact.getPersonalPhoneNumber());
        ContactProfile expectedContact  = new ContactProfile(contact.getFirstName(),contact.getLastName(),contact.getTitle(),phoneNumber,emailAddress,user);
        when(jwtService.extractEmail(" ")).thenReturn(user.getEmail());
        when(authRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(contactRepository.save(any(ContactProfile.class))).thenReturn(expectedContact);

      ContactProfile  savedContact = contactServiceImpl.saveContact(contact," ");
        assertNotNull(savedContact);
        assertEquals(savedContact,expectedContact);
        verify(authRepository, times(1)).findByEmail(user.getEmail());
        verify(contactRepository, times(1)).save(any(ContactProfile.class));

    }
    @Test
     void SaveContact_Failure() {

        String token = "Token";
        String userEmail = "nonexistentuser@example.com";
        Contactdto contact = new Contactdto("testUser","user","test@example.com","test1@example.com","test2@example.com",1,0,0,0,"test123");

        when(jwtService.extractEmail(token)).thenReturn(userEmail);
        when(authRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            contactServiceImpl.saveContact(contact, token);
        });

        verify(contactRepository, never()).save(any(ContactProfile.class));
    }
    @Test
     void DeleteContact_Success() {
        // Given
        long contactId = 1L;
        ContactProfile existingContact = new ContactProfile();
        existingContact.setProfileId(contactId);


        when(contactRepository.findById(contactId)).thenReturn(Optional.of(existingContact));


        ContactProfile result = contactServiceImpl.deleteContact(contactId);
        assertNotNull(result);
        assertEquals(contactId, result.getProfileId());
        verify(contactRepository, times(1)).findById(contactId);
        verify(contactRepository, times(1)).deleteById(contactId);
    }
    @Test
     void DeleteContact_Failure() {
        long contactId = 1L;


        when(contactRepository.findById(contactId)).thenReturn(Optional.empty());


        assertThrows(ContactNotFoundException.class, () -> {
            contactServiceImpl.deleteContact(contactId);
        });


        verify(contactRepository, times(1)).findById(contactId);
        verify(contactRepository, never()).deleteById(contactId);
    }

    @Test
     void UpdateContact_Success() {

        Long contactId = 1L;
        User user = new User(1L, "testUser", "test123", "test@gmail.com");

        Contactdto contact = new Contactdto("testUser","user","test@example.com","test1@example.com","test2@example.com",1,0,0,0,"test123");

        EmailAddress emailAddress = new EmailAddress(contact.getWorkEmailAddress(),contact.getPersonalEmailAddress(),contact.getOtherEmailAddress());
        PhoneNumber phoneNumber  = new PhoneNumber(contact.getWorkPhoneNumber(),contact.getHomePhoneNumber(),contact.getPersonalPhoneNumber());
        ContactProfile existingContact  = new ContactProfile(contact.getFirstName(),contact.getLastName(),contact.getTitle(),phoneNumber,emailAddress,user);

        when(contactRepository.findById(contactId)).thenReturn(Optional.of(existingContact));
        when(contactRepository.save(existingContact)).thenReturn(existingContact);


        ContactProfile updatedContact = contactServiceImpl.updateContact(contact, contactId);


        assertNotNull(updatedContact);
        assertEquals(contact.getFirstName(), updatedContact.getFirstName());
        assertEquals(contact.getWorkEmailAddress(), updatedContact.getEmailAddresses().getWorkEmail());
        assertEquals(contact.getPersonalEmailAddress(), updatedContact.getEmailAddresses().getPersonalEmail());
        assertEquals(contact.getOtherEmailAddress(), updatedContact.getEmailAddresses().getOtherEmail());

        verify(contactRepository, times(1)).findById(contactId);
        verify(contactRepository, times(1)).save(existingContact);
    }
    @Test
     void UpdateContact_Failure() {

        Long contactId = 1L;
        Contactdto contact = new Contactdto("testUser","user","test@example.com","test1@example.com","test2@example.com",1,0,0,0,"test123");


        when(contactRepository.findById(contactId)).thenReturn(Optional.empty());


        assertThrows(ContactNotFoundException.class, () ->
                contactServiceImpl.updateContact(contact, contactId)
        );

        verify(contactRepository, times(1)).findById(contactId);
        verify(contactRepository, never()).save(any(ContactProfile.class));
    }
   @Test
   void contactImport_Success() throws IOException {

      long userId = 1L;
      User user = new User(1L, "testUser", "test123", "test@gmail.com");

      String vcardContent = "BEGIN:VCARD\nVERSION:4.0\nFN:John Doe\nEMAIL;TYPE=HOME:john@example.com\nTEL;TYPE=CELL:1234567890\nEND:VCARD";
      MockMultipartFile file = new MockMultipartFile("file", "contacts.vcf", "text/vcard", new ByteArrayInputStream(vcardContent.getBytes()));


      when(authRepository.findById(userId)).thenReturn(Optional.of(user));


      ResponseEntity<Response> responseEntity = contactServiceImpl.contactImport(file, userId);


      assertNotNull(responseEntity);
      assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
      assertEquals("Contacts Imported Successfully", responseEntity.getBody().getMessage());


      verify(authRepository, times(1)).findById(userId);
      verify(contactRepository, atLeastOnce()).save(any(ContactProfile.class));
   }


}
