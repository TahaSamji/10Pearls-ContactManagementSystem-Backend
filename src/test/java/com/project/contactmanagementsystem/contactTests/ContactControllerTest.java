package com.project.contactmanagementsystem.contactTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.contactmanagementsystem.auth.customexceptions.UserNotFoundException;
import com.project.contactmanagementsystem.auth.models.User;
import com.project.contactmanagementsystem.contact.controller.ContactController;
import com.project.contactmanagementsystem.contact.customexceptions.ContactNotFoundException;
import com.project.contactmanagementsystem.contact.dto.Contactdto;
import com.project.contactmanagementsystem.contact.models.ContactProfile;
import com.project.contactmanagementsystem.contact.service.ContactService;
import com.project.contactmanagementsystem.exceptionhandler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
class ContactControllerTest {
    @Mock
    private ContactService contactService;
    @InjectMocks
    private ContactController contactController;
    private MockMvc mockMvc;
    User user = new User(1L, "Taha", "hello", "taha@gmail.com");
    ContactProfile contact1 = new ContactProfile(1L, "John", "Doe", "john.doe@example.com", user);
    ContactProfile contact2 = new ContactProfile(2L, "Jane", "Smith", "jane.smith@example.com", user);
    Contactdto contactDto = new Contactdto("testUser","user","test@example.com","test1@example.com","test2@example.com",1,0,0,0,"test123");
    ContactProfile contact = new ContactProfile(contactDto.getProfileId(), contactDto.getFirstName(), contactDto.getLastName(), contactDto.getTitle(), user);
    ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contactController).setControllerAdvice(new GlobalExceptionHandler()).build(); // Initialize MockMvc with the controller instance
    }
    @Test
    void getAllContacts_success() throws Exception {
        List<ContactProfile> contactList = Arrays.asList(contact1, contact2);
        when(contactService.viewMyContacts(user.getId(),"",0,2)).thenReturn(contactList);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/showcontacts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));
    }
    @Test
    void getAllContacts_Failure() throws Exception {
        when(contactService.viewMyContacts(user.getId(), "", 0, 2)).thenThrow(new UserNotFoundException());
        mockMvc.perform(MockMvcRequestBuilders.get("/showcontacts")
                        .param("userId", "1")
                        .param("search", "")
                        .param("page", "0")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void CreateContact_Success() throws Exception {
        String token = "token";
        String contactJson = objectMapper.writeValueAsString(contactDto);
        when(contactService.saveContact(contactDto, token)).thenReturn(contact);
        mockMvc.perform(MockMvcRequestBuilders.post("/addcontact").content(contactJson).header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    void CreateContact_Failure() throws Exception {
        String token = "token";
        String contactJson = objectMapper.writeValueAsString(contactDto);
        when(contactService.saveContact(contactDto, token)).thenThrow(new UserNotFoundException());
        mockMvc.perform(MockMvcRequestBuilders.post("/addcontact").content(contactJson).header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())

                .andExpect(status().isNotFound());
    }
    @Test
    void UpdateContact_Failure() throws Exception {
        String contactJson = objectMapper.writeValueAsString(contactDto);
        when(contactService.updateContact(contactDto, contactDto.getProfileId())).thenThrow(new UserNotFoundException());
        mockMvc.perform(MockMvcRequestBuilders.post("/updatecontact").content(contactJson).param("contactId", String.valueOf(contactDto.getProfileId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User Not found"));
        verify(contactService, times(1)).updateContact(contactDto, contactDto.getProfileId());
    }
    @Test
    void UpdateContact_Success() throws Exception {
        long contactId = 1L;
        String contactJson = objectMapper.writeValueAsString(contactDto);
        when(contactService.updateContact(contactDto, contactId)).thenReturn(contact);
        mockMvc.perform(MockMvcRequestBuilders.post("/updatecontact")
                        .content(contactJson)
                        .param("contactId", String.valueOf(contactId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.profileId").value(contact.getProfileId()))
                .andExpect(jsonPath("$.firstName").value(contact.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(contact.getLastName()))
                .andExpect(jsonPath("$.title").value(contact.getTitle()));
        verify(contactService, times(1)).updateContact(contactDto, contactId);
    }
    @Test
    void SearchContact_Failure() throws Exception {
        String searchedValue = "Jane";
        List<ContactProfile> contactList = Collections.singletonList(contact2);
        when(contactService.viewMyContacts(user.getId(),searchedValue,0,2)).thenReturn(contactList);
        mockMvc.perform(MockMvcRequestBuilders.get("/showcontacts")
                        .param("userId", String.valueOf(user.getId()))
                        .param("value", searchedValue)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].firstName").value(searchedValue));
        verify(contactService, times(1)).viewMyContacts(user.getId(),searchedValue,0,2);
    }
    @Test
    void DeleteContact_Success() throws Exception {
        long contactId = contact.getProfileId();
        when(contactService.deleteContact(contactId)).thenReturn(contact);
        mockMvc.perform(MockMvcRequestBuilders.post("/delectcontact")
                        .param("contactId", String.valueOf(contactId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Contact has been Deleted"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(contactService, times(1)).deleteContact(contactId);
    }
    @Test
    void DeleteContact_Failure() throws Exception {
        when(contactService.deleteContact(contact.getProfileId())).thenThrow(new ContactNotFoundException("Contact Not Found"));
        mockMvc.perform(MockMvcRequestBuilders.post("/delectcontact")
                        .param("contactId", String.valueOf(contact.getProfileId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Contact Not Found"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(contactService, times(1)).deleteContact(contact.getProfileId());
    }
    @Test
    void ImportContact_Failure() throws Exception {

        String vcardContent = "BEGIN:VCARD\nVERSION:4.0\nFN:John Doe\nEMAIL;TYPE=HOME:john@example.com\nTEL;TYPE=CELL:1234567890\nEND:VCARD";
        MockMultipartFile file = new MockMultipartFile("file", "contacts.vcf", "text/vcard", new ByteArrayInputStream(vcardContent.getBytes()));


        when(contactService.contactImport(any(MockMultipartFile.class), anyLong())).thenThrow(new UserNotFoundException());


        mockMvc.perform(MockMvcRequestBuilders.multipart("/contactimport")
                        .file(file)
                        .param("userId", String.valueOf(user.getId()))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User Not found"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


        verify(contactService, times(1)).contactImport(any(MockMultipartFile.class), eq(user.getId()));
    }

}