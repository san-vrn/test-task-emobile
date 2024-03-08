package com.example.effectivemobile.test.authentication;

import com.example.effectivemobile.test.authentication.request.AuthenticationRequest;
import com.example.effectivemobile.test.authentication.request.ChangePasswordRequest;
import com.example.effectivemobile.test.authentication.request.OauthPartnerRequest;
import com.example.effectivemobile.test.authentication.request.RegisterRequest;
import com.example.effectivemobile.test.authentication.responce.AuthenticationResponse;
import com.example.effectivemobile.test.entity.bank.account.BankAccount;
import com.example.effectivemobile.test.entity.user.Role;
import com.example.effectivemobile.test.entity.user.User;
import com.example.effectivemobile.test.entity.token.Token;
import com.example.effectivemobile.test.entity.token.TokenType;
import com.example.effectivemobile.test.exception.UserIsExsistsRequestException;
import com.example.effectivemobile.test.exception.UserNotFoundException;
import com.example.effectivemobile.test.repository.BankAccountRepository;
import com.example.effectivemobile.test.repository.TokenRepository;
import com.example.effectivemobile.test.repository.UserRepository;
import com.example.effectivemobile.test.service.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;

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

  public AuthenticationResponse register(RegisterRequest request) {
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
            .money(request.getInitialAmountBankAccount())
            .maxDeposit(request.getInitialAmountBankAccount()*3.07)
            .build();

    var savedUser = userRepository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    var saveUserBankAccount = bankAccountRepository.save(bankAccount);
    saveUserToken(savedUser, jwtToken);

    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getLogin(),
                    request.getPassword()
            )
    );
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

    public AuthenticationResponse oauthAuthenticate(OauthPartnerRequest request) {
    final String jwt;
    String userEmail;

    jwt = request.getJwtToken();
    if (!jwtService.validateToken(jwt)){throw new JwtException("Не валидный токен");}

    userEmail = jwtService.getYandexEmail(jwt);

     var user = userRepository.findByEmail(userEmail);
    if(!user.isPresent()){
      var saveUser = User.builder()
              .email(userEmail)
              .role(Role.USER)
              .createdAt(Timestamp.valueOf(java.time.LocalDateTime.now()))
              .password(passwordEncoder.encode("1q2w3e4r")) //to do
              .build();
      userRepository.save(saveUser);
    }
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
      var jwtToken = jwtService.generateToken(userDetails);
      var refreshToken = jwtService.generateRefreshToken(userDetails);
      revokeAllUserTokens(user.get());
      saveUserToken(user.get(), jwtToken);
      /*  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                )
        );*/
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );/*
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );*/
      Authentication authentication = authToken;
        SecurityContextHolder.getContext().setAuthentication(authentication);
      return AuthenticationResponse.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .build();
  }
  
  public ResponseEntity changePassword (ChangePasswordRequest changePasswordRequest){
    // TODO: 08.12.2023
    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
    return responseEntity;
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
