package com.example.effectivemobile.test.service.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserFilterRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate dateBirthDay;
    private String phone;
    private String fullName;
    private String email;

    public Boolean isEmpty(){
        if(this.dateBirthDay != null){return false;}
        if(this.phone!= null && !this.phone.isBlank()){return false;}
        if(this.fullName!= null && !this.fullName.isBlank()){return false;}
        if(this.email!= null && !this.email.isBlank()){return false;}
        else return true;
    }
}
