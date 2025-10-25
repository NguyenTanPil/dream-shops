package com.felixnguyen.dreamshops.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felixnguyen.dreamshops.repository.JwtResponse;
import com.felixnguyen.dreamshops.request.LoginRequest;
import com.felixnguyen.dreamshops.response.ApiResponse;
import com.felixnguyen.dreamshops.security.jwt.JwtUtils;
import com.felixnguyen.dreamshops.security.user.ShopUserDetails;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth APIs", description = "APIs for managing auth")
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateTokenForUser(authentication);
      ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
      JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
      return ResponseEntity.ok(new ApiResponse("Login successful", jwtResponse));
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
