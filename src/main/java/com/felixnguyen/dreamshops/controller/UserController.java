package com.felixnguyen.dreamshops.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felixnguyen.dreamshops.dto.UserDto;
import com.felixnguyen.dreamshops.exceptions.AlreadyExistsException;
import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.request.CreateUserRequest;
import com.felixnguyen.dreamshops.request.UpdateUserRequest;
import com.felixnguyen.dreamshops.response.ApiResponse;
import com.felixnguyen.dreamshops.service.user.IUserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "User APIs", description = "APIs for managing users")
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
  private final IUserService userService;

  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse> getUser(@PathVariable Long userId) {
    try {
      UserDto user = userService.getUserById(userId);
      return ResponseEntity.ok().body(new ApiResponse("Get user success", user));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PostMapping
  public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
    System.out.println("FirstName = " + request.getFirstName());
    System.out.println("LastName = " + request.getLastName());
    System.out.println("Email = " + request.getEmail());
    System.out.println("Password = " + request.getPassword());
    try {
      UserDto user = userService.createUser(request);
      return ResponseEntity.ok().body(new ApiResponse("Create user success", user));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(409).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PatchMapping("/{userId}")
  public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest request) {
    try {
      UserDto user = userService.updateUser(userId, request);
      return ResponseEntity.ok().body(new ApiResponse("Update user success", user));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
    try {
      userService.deleteUser(userId);
      return ResponseEntity.ok().body(new ApiResponse("Delete user success", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
    }
  }

}
