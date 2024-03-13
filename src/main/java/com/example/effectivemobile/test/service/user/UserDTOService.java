package com.example.effectivemobile.test.service.user;

import com.example.effectivemobile.test.dto.UserDTO;
import com.example.effectivemobile.test.entity.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDTOService {

    public UserDTO  mappingUserToUserDTO(User user){
        return new UserDTO(user);
    }

    public List<UserDTO> listMappingUserToUserDTO(List<User> users){
       return users.stream().map(this::mappingUserToUserDTO).collect(Collectors.toList());
    }
}
