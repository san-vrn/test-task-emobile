package com.example.effectivemobile.test.repository;

import com.example.effectivemobile.test.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByLogin(String login);
    Optional<User> findByPhone(String phone);
    Optional<List<User>> findByFullName(String fullName);
    Optional<List<User>> findByBirthDate(LocalDate birthDate);
}
