package com.example.effectivemobile.test.service.user;

import com.example.effectivemobile.test.dto.UserDTO;
import com.example.effectivemobile.test.entity.user.User;
import com.example.effectivemobile.test.exception.request.RequestIsEmpty;
import com.example.effectivemobile.test.exception.user.UserNotFoundException;
import com.example.effectivemobile.test.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserDTOService userDTOService;

    @Autowired
    public UserService(UserRepository userRepository, UserDTOService userDTOService) {
        this.userRepository = userRepository;
        this.userDTOService = userDTOService;
    }

    public List<User> listAll(){
        return userRepository.findAll();
    }
    public Optional<User> findByUserId(Long id){return userRepository.findById(id);}
    public Optional<User> findByUserEmail(String email){return userRepository.findByEmail(email);}
    public UserDTO filterByUserEmail(String email){
        var user = userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException(email, "Пользователь не найден"));
        return new UserDTO(user);}

    public UserDTO filterByUserPhone(String phone){
        var user = userRepository.findByPhone(phone).orElseThrow(()-> new UserNotFoundException(phone, "Пользователь не найден"));
        return new UserDTO(user);}

    public Page<UserDTO> findByUserBirthDay(LocalDate date, Pageable pageable){
        var users = userRepository.findAll(UserFilterSpecification.hasBirthDay(date), pageable);
        return users.map(userDTOService::mappingUserToUserDTO);
    }


    public Optional<User> findByPhone(String phone){return userRepository.findByPhone(phone);}
    public Optional<List<User>> findByUserFullName(String fullName){return userRepository.findByFullName(fullName);}
    public Optional<List<User>> findByBirthDay(LocalDate birthDate){return userRepository.findByBirthDate(birthDate);}

    public Page<UserDTO> filterUser(String name, Pageable pageable){
        var users = userRepository.findAll(UserFilterSpecification.hasUserName(name), pageable);
        return users.map(userDTOService::mappingUserToUserDTO);

    }



    public List<UserDTO> filterByBirthDay(UserFilterRequest userFilterRequest){

        if(userFilterRequest.isEmpty()){throw new RequestIsEmpty(userFilterRequest.toString());}

        List<User> users = userRepository.findByOverBirthDate(userFilterRequest.getDateBirthDay());
        if(users.isEmpty()){throw new UserNotFoundException("пользователи с датой рождения позднее " + userFilterRequest.getDateBirthDay().toString() + " не найдены","не найден");}

        List<UserDTO> userDTOs = new ArrayList<>();
            for(User user: users){
                userDTOs.add(new UserDTO(user));
        }
            return userDTOs;
    }

}
