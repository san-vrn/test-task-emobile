package com.example.effectivemobile.test.service;

import com.example.effectivemobile.test.entity.user.User;
import com.example.effectivemobile.test.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> listAll(){
        return userRepository.findAll();
    }
    public Optional<User> findByUserId(Long id){return userRepository.findById(id);}
    public Optional<User> findByUserEmail(String email){return userRepository.findByEmail(email);}
    public Optional<User> findByPhone(String phone){return userRepository.findByPhone(phone);}
    public Optional<List<User>> findByUserFullName(String fullName){return userRepository.findByFullName(fullName);}
    public Optional<List<User>> findByBirthDay(LocalDate birthDate){return userRepository.findByBirthDate(birthDate);}


}
