package com.example.effectivemobile.test.authentication;

import com.example.effectivemobile.test.authentication.request.AuthenticationRequest;
import com.example.effectivemobile.test.authentication.request.ChangePasswordRequest;
import com.example.effectivemobile.test.authentication.request.OauthPartnerRequest;
import com.example.effectivemobile.test.authentication.request.RegisterRequest;
import com.example.effectivemobile.test.authentication.responce.AuthenticationResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/oauthauthenticate")
  public ResponseEntity<AuthenticationResponse> oauthAuthenticate(
          @RequestBody OauthPartnerRequest request
          ) {
      return ResponseEntity.ok(service.oauthAuthenticate(request));
  }

  @PostMapping("/changepass")
  public ResponseEntity changePassword(
          @RequestBody ChangePasswordRequest request
  ) {
    // TODO: 08.12.2023  
    return ResponseEntity.ok(service.changePassword(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }


}
