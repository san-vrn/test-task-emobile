package com.example.effectivemobile.test.controller;

import com.example.effectivemobile.test.dto.UserDTO;
import com.example.effectivemobile.test.service.user.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/user/find")
@RequiredArgsConstructor
public class UserFindController {


    private UserService userService;

    @Autowired
    public UserFindController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String get() {
        return "Hello world";
    }

    @GetMapping(value = "/username/{userName}")
    public Page<UserDTO> getUsersByUsername(@PathVariable String userName, Pageable pageable) {return userService.filterUser(userName, pageable);}

    @GetMapping(value = "/useremail/{userEmail}")
    public UserDTO getUsersByUserEmail(@PathVariable String userEmail) {return userService.filterByUserEmail(userEmail);}

    @GetMapping(value = "/userphone/{userPhone}")
    public UserDTO getUsersByUserPhone(@PathVariable String userPhone) {return userService.filterByUserEmail(userPhone);}

    @GetMapping(value = "/userbirthday/{userBirthDay}")
    public Page<UserDTO> getUsersByUserPhone(
            @PathVariable
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
            LocalDate userBirthDay,
            Pageable pageable
    ) {return userService.findByUserBirthDay(userBirthDay,pageable);}

}
