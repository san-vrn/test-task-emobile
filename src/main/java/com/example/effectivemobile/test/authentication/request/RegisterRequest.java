package com.example.effectivemobile.test.authentication.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String login;
  private String phone;
  private String email;
  private String password;
  private Double initialAmountBankAccount;
  private LocalDate birthDate;
  private String fullname;
}
