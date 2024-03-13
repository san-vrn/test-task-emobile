package com.example.effectivemobile.test.authentication;

import com.example.effectivemobile.test.authentication.request.AuthenticationRequest;
import com.example.effectivemobile.test.authentication.request.RegisterRequest;
import com.example.effectivemobile.test.authentication.responce.AuthenticationResponse;
import com.example.effectivemobile.test.entity.bank.account.BankAccount;
import com.example.effectivemobile.test.entity.user.Role;
import com.example.effectivemobile.test.entity.user.User;
import com.example.effectivemobile.test.entity.token.Token;
import com.example.effectivemobile.test.entity.token.TokenType;
import com.example.effectivemobile.test.exception.user.UserIsExsistsRequestException;
import com.example.effectivemobile.test.exception.user.UserNotFoundException;
import com.example.effectivemobile.test.repository.BankAccountRepository;
import com.example.effectivemobile.test.repository.TokenRepository;
import com.example.effectivemobile.test.repository.UserRepository;
import com.example.effectivemobile.test.service.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final BankAccountRepository bankAccountRepository;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public ResponseEntity register(RegisterRequest request) {
    if(userRepository.findByEmail(request.getEmail()).isPresent()){throw new UserIsExsistsRequestException("Пользователь с таким email уже существует",request.getEmail());}
    if(userRepository.findByPhone(request.getPhone()).isPresent()){throw new UserIsExsistsRequestException("Пользователь с таким номером телефона уже существует",request.getPhone());}
    if(userRepository.findByLogin(request.getLogin()).isPresent()){throw new UserIsExsistsRequestException("Пользователь с таким логином уже существует",request.getLogin());}

    var user = User.builder()
            .email(request.getEmail())
            .phone(request.getPhone())
            .login(request.getLogin())
            .birthDate(request.getBirthDate())
            .fullName(request.getFullname())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .createdAt(Timestamp.valueOf(java.time.LocalDateTime.now()))
            .build();

    var bankAccount = BankAccount.builder()
            .user(user)
            .deposit(request.getInitialAmountBankAccount())
            .maxDeposit(request.getInitialAmountBankAccount()*3.07)
            .build();

    userRepository.save(user);
    bankAccountRepository.save(bankAccount);

    return new ResponseEntity(HttpStatus.OK);
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    var user = userRepository.findByLogin(request.getLogin())
            .orElseThrow(()-> new UserNotFoundException(request.getLogin(),"User not found"));
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }
  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user);
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.userRepository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
