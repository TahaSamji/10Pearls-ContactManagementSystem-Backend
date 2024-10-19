package com.project.ContactManagementSystem.auth.repositories;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ContactManagementSystem.auth.models.User;


@Repository
public interface AuthRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}