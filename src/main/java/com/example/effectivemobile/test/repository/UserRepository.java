package com.example.effectivemobile.test.repository;

import com.example.effectivemobile.test.entity.user.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findByLogin(String login);
    Optional<User> findByPhone(String phone);
    Optional<List<User>> findByFullName(String fullName);
    Optional<List<User>> findByBirthDate(LocalDate birthDate);

    @Query(value = "select u from User u where u.birthDate > ?1")
    List<User> findByOverBirthDate(LocalDate date);
}
