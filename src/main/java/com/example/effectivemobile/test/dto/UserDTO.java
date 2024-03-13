package com.example.effectivemobile.test.dto;


import com.example.effectivemobile.test.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
public class UserDTO {

    private String fullname;
    private String email;
    private String phone;
    private LocalDate birthDay;

    public UserDTO(User user) {
        this.fullname = user.getFullName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.birthDay = user.getBirthDate();
    }
}
