package com.project.ContactManagementSystem.ContactTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ContactManagementSystem.auth.customexceptions.UserNotFoundException;
import com.project.ContactManagementSystem.auth.models.User;
import com.project.ContactManagementSystem.auth.repositories.AuthRepository;
import com.project.ContactManagementSystem.auth.service.JwtService;
import com.project.ContactManagementSystem.contact.controller.ContactController;
import com.project.ContactManagementSystem.contact.dto.Contactdto;
import com.project.ContactManagementSystem.contact.models.ContactProfile;
import com.project.ContactManagementSystem.contact.repositories.ContactRepository;
import com.project.ContactManagementSystem.contact.service.ContactService;
import com.project.ContactManagementSystem.exceptionhandler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ContactControllerTest {

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
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contactController).setControllerAdvice(new GlobalExceptionHandler()).build(); // Initialize MockMvc with the controller instance
    }

    @Test
    public void getAllContacts_success() throws Exception {
        List<ContactProfile> contactList = Arrays.asList(contact1, contact2);
        when(contactService.ViewMyContacts(user.getId(),"",0,2)).thenReturn(contactList);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/showcontacts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));
    }
    @Test
    public void getAllContacts_Failure() throws Exception {
        when(contactService.ViewMyContacts(user.getId(), "", 0, 2)).thenThrow(new UserNotFoundException("User not found"));


        mockMvc.perform(MockMvcRequestBuilders.get("/showcontacts")
                        .param("userId", "1")
                        .param("search", "")
                        .param("page", "0")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void CreateContact_Success() throws Exception {
        String token = "token";
        String contactJson = objectMapper.writeValueAsString(contactDto);

        when(contactService.SaveContact(contactDto, token)).thenReturn(contact);


        mockMvc.perform(MockMvcRequestBuilders.post("/addcontact").content(contactJson).header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());
    }
    @Test
    public void CreateContact_Failure() throws Exception {
        String token = "token";
        String contactJson = objectMapper.writeValueAsString(contactDto);

        when(contactService.SaveContact(contactDto, token)).thenThrow(new UserNotFoundException("User not found"));


        mockMvc.perform(MockMvcRequestBuilders.post("/addcontact").content(contactJson).header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }
    @Test
    public void UpdateContact_Failure() throws Exception {
        String token = "token";
        String contactJson = objectMapper.writeValueAsString(contactDto);

        when(contactService.UpdateContact(contactDto, contact.getProfileId())).thenThrow(new UserNotFoundException("User not found"));


        mockMvc.perform(MockMvcRequestBuilders.post("/updatecontact").content(contactJson).param("ContactId", String.valueOf(contact.getProfileId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void UpdateContact_Success() throws Exception {
        String token = "token";
        String contactJson = objectMapper.writeValueAsString(contactDto);

        when(contactService.SaveContact(contactDto, token)).thenThrow(new UserNotFoundException("User not found"));


        mockMvc.perform(MockMvcRequestBuilders.post("/addcontact").content(contactJson).header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }
}
