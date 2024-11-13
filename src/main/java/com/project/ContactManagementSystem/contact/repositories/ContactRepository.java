package com.project.ContactManagementSystem.contact.repositories;

import com.project.ContactManagementSystem.auth.models.User;
import com.project.ContactManagementSystem.contact.dto.Contactdto;
import com.project.ContactManagementSystem.contact.models.ContactProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<ContactProfile, Long> {
//    public List<ContactProfile> findbytitle(long userid, String token);
      Optional<ContactProfile> findById(long ContactId);
      @Query("SELECT c FROM ContactProfile c " +"WHERE c.user.id = ?2 "+
              "AND (LOWER(c.firstName) LIKE %?1% " +
              "OR LOWER(c.lastName) LIKE %?1% " +
              "OR LOWER(c.title) LIKE %?1% )")
      List<ContactProfile> search(String value,long userId,Pageable pageable);

      List<ContactProfile> findAllByUser(User user, Pageable pageable);
      List<ContactProfile> findAllByUser(User user);


}
