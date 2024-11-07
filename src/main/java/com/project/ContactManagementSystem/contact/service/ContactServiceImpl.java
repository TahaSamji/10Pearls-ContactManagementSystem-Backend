package com.project.ContactManagementSystem.contact.service;

import com.project.ContactManagementSystem.auth.customexceptions.UserNotFoundException;
import com.project.ContactManagementSystem.auth.models.User;
import com.project.ContactManagementSystem.auth.repositories.AuthRepository;
import com.project.ContactManagementSystem.auth.service.JwtService;
import com.project.ContactManagementSystem.contact.customexceptions.ContactNotFoundException;
import com.project.ContactManagementSystem.contact.dto.Contactdto;
import com.project.ContactManagementSystem.contact.models.ContactProfile;
import com.project.ContactManagementSystem.contact.models.EmailAddress;
import com.project.ContactManagementSystem.contact.models.PhoneNumber;
import com.project.ContactManagementSystem.contact.repositories.ContactRepository;
import com.project.ContactManagementSystem.dto.Response;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.parameter.EmailType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Email;
import ezvcard.property.FormattedName;
import ezvcard.property.Telephone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
@Slf4j
@Service
public class ContactServiceImpl  implements ContactService{


    private final ContactRepository contactRepository;

    private final AuthRepository authRepository;

    private final JwtService jwtService;

    public ContactServiceImpl(ContactRepository contactRepository, AuthRepository authRepository, JwtService jwtService) {
        this.contactRepository = contactRepository;
        this.authRepository = authRepository;
        this.jwtService = jwtService;
    }

    @Override
    public ContactProfile SaveContact(Contactdto contact,String token) {
       String email  = jwtService.extractUserName(token);
       User user = authRepository.findByEmail(email).orElseThrow( ()->new UserNotFoundException("User Not Found"));

        EmailAddress emailAddress = new EmailAddress(contact.getWorkEmailAddress(),contact.getPersonalEmailAddress(),contact.getOtherEmailAddress());
        PhoneNumber phoneNumber  = new PhoneNumber(contact.getWorkPhoneNumber(),contact.getHomePhoneNumber(),contact.getPersonalPhoneNumber());
        ContactProfile newContact  = new ContactProfile(contact.getFirstName(),contact.getLastName(),contact.getTitle(),phoneNumber,emailAddress,user);
       log.info("Contact Saved");
       return contactRepository.save(newContact);
    }

    @Override
    public List<ContactProfile> ViewMyContacts(String token,int pageNo, int pageSize) {
        String email  = jwtService.extractUserName(token);
        User user = authRepository.findByEmail(email).orElseThrow( ()->new UserNotFoundException("User Not Found"));
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<ContactProfile>  contacts = contactRepository.findAllByUser(user,pageable);


        return contacts;
    }

    @Override
    public ContactProfile UpdateContact(Contactdto contact,Long ContactId) {
        ContactProfile existingContact = contactRepository.findById(ContactId).orElseThrow(() -> new ContactNotFoundException("Contact Not Found"));
        existingContact.setFirstName(contact.getFirstName());
        existingContact.setLastName((contact.getLastName()));
        existingContact.setTitle(contact.getTitle());
        PhoneNumber phoneNumber  = new PhoneNumber(contact.getWorkPhoneNumber(),contact.getHomePhoneNumber(),contact.getPersonalPhoneNumber());
        EmailAddress emailAddress = new EmailAddress(contact.getWorkEmailAddress(),contact.getPersonalEmailAddress(),contact.getOtherEmailAddress());

        existingContact.setPhoneNumbers(phoneNumber);
        existingContact.setEmailAddresses(emailAddress);
        contactRepository.save(existingContact);
        log.info("Contact Updated");

        return existingContact;
    }
    public ContactProfile DeleteContact(long ContactId) {

        ContactProfile existingContact = contactRepository.findById(ContactId).orElseThrow(() -> new ContactNotFoundException("Contact Not Found"));

        contactRepository.deleteById(ContactId);
        log.info("Contact Deleted");
        return existingContact;
    }

    @Override
    public List<ContactProfile> SearchContact(String value,long userId) {
    String  LowerCaseValue = value.toLowerCase(Locale.ROOT);

       List<ContactProfile> MySearch = contactRepository.search(LowerCaseValue,userId);
        log.info("Search Completed Returning Result ");
        return MySearch;
    }

    @Override
    public ResponseEntity<byte[]> ExportContact(long userId) {
        User user = authRepository.findById(userId).orElseThrow( ()->new UserNotFoundException("User Not Found"));
        List<ContactProfile>  contacts = contactRepository.findAllByUser(user);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (ContactProfile contactProfile : contacts) {
            VCard card = new VCard();
            card.addProperty(new FormattedName(contactProfile.getFirstName() + " " +contactProfile.getLastName()));
            Email personalEmail = new Email(contactProfile.getEmailAddresses().getPersonalEmail());
            personalEmail.getTypes().add(EmailType.HOME);
            card.addProperty(personalEmail);
            Email workEmail = new Email(contactProfile.getEmailAddresses().getWorkEmail());
            workEmail.getTypes().add(EmailType.WORK);
            card.addProperty(workEmail);

            Telephone homeNumber = new Telephone(String.valueOf(contactProfile.getPhoneNumbers().getHomeNumber()));
            homeNumber.getTypes().add(TelephoneType.HOME);
            card.addProperty(homeNumber);
            Telephone workNumber = new Telephone(String.valueOf(contactProfile.getPhoneNumbers().getWorkNumber()));
            workNumber.getTypes().add(TelephoneType.WORK);
            card.addProperty(workNumber);
            Telephone personalNumber = new Telephone(String.valueOf(contactProfile.getPhoneNumbers().getPersonalNumber()));
            personalNumber.getTypes().add(TelephoneType.CELL);
            card.addProperty(personalNumber);

            try {
                Ezvcard.write(card).go(outputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


        byte[] outputData = outputStream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contacts.vcf");

    return  ResponseEntity.ok()
                .headers(headers)
                .contentLength(outputData.length)
                .contentType(MediaType.parseMediaType("text/vcard"))
                .body(outputData);
    }
    @Override
    public ResponseEntity<Response> importContact(MultipartFile file, long userId) throws IOException {
        User user = authRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found"));
        List<VCard> vcards = Ezvcard.parse(file.getInputStream()).all();

        for (VCard vcard : vcards) {
            ContactProfile contact = new ContactProfile();
            contact.setUser(user);


            FormattedName names = vcard.getFormattedName();
            if (names != null) {
                String[] arr = names.getValue().split(" ");
                contact.setFirstName(arr[0]);
                contact.setLastName( arr[1]);
            }


            EmailAddress allEmail = new EmailAddress();
            PhoneNumber allNumbers = new PhoneNumber();


            List<Email> emails = vcard.getEmails();
            for (Email email : emails) {
                String type = email.getParameter("TYPE");
                String emailAddress = email.getValue();
                if (type.equalsIgnoreCase("HOME")) {
                    allEmail.setPersonalEmail(emailAddress);
                } else if (type.equalsIgnoreCase("WORK")) {
                    allEmail.setWorkEmail(emailAddress);
                } else {
                    allEmail.setOtherEmail(emailAddress);
                }
            }
            contact.setEmailAddresses(allEmail); // Set after loop

            // Process phone numbers
            List<Telephone> telephoneNumbers = vcard.getTelephoneNumbers();
            for (Telephone number : telephoneNumbers) {
                String type = number.getParameter("TYPE");
                String Number = number.getText();
                try {
                    long parsedNumber = Long.parseLong(Number);
                    if (type.equalsIgnoreCase("HOME")) {
                        allNumbers.setHomeNumber(parsedNumber);
                    } else if (type.equalsIgnoreCase("WORK")) {
                        allNumbers.setWorkNumber(parsedNumber);
                    } else if (type.equalsIgnoreCase("CELL")) {
                        allNumbers.setPersonalNumber(parsedNumber);
                    }
                } catch (NumberFormatException e) {
                    log.warn("Invalid phone number format: {}", Number);
                }
            }
            contact.setPhoneNumbers(allNumbers);


            contactRepository.save(contact);
            log.info("Contact Saved in database");
        }

        return new ResponseEntity<>(new Response("Contacts Imported Successfully"), HttpStatusCode.valueOf(200));
    }


}
