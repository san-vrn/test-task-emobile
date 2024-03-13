package com.example.effectivemobile.test.service.user;

import com.example.effectivemobile.test.entity.user.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserFilterSpecification {

    static Specification<User> hasUserName(String userName) {
        return (user, cq, cb) -> cb.like(user.get("fullName"), "%"+userName+"%");
    }

    static Specification<User> hasBirthDay(LocalDate date) {
        return (user, cq, cb) -> cb.greaterThan(user.get("birthDate"), date);
    }

}
