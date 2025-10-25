package com.felixnguyen.dreamshops.service.user;

import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.felixnguyen.dreamshops.dto.UserDto;
import com.felixnguyen.dreamshops.exceptions.AlreadyExistsException;
import com.felixnguyen.dreamshops.exceptions.ResourceNotFoundException;
import com.felixnguyen.dreamshops.model.Role;
import com.felixnguyen.dreamshops.model.User;
import com.felixnguyen.dreamshops.repository.RoleRepository;
import com.felixnguyen.dreamshops.repository.UserRepository;
import com.felixnguyen.dreamshops.request.CreateUserRequest;
import com.felixnguyen.dreamshops.request.UpdateUserRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  private UserDto convertDto(User user) {
    return modelMapper.map(user, UserDto.class);
  }

  @Override
  public UserDto getUserById(Long id) {
    return userRepository.findById(id).map(this::convertDto)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
  }

  @Override
  public User getOriginUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
  }

  @Override
  public UserDto createUser(CreateUserRequest request) {
    return Optional.of(request).filter(user -> !userRepository.existsByEmail(user.getEmail()))
        .map(req -> {
          User newUser = new User();
          Role userRole = roleRepository.findByName("ROLE_USER")
              .orElseThrow(() -> new RuntimeException("Default role not found"));
          newUser.setFirstName(request.getFirstName());
          newUser.setLastName(request.getLastName());
          newUser.setEmail(request.getEmail());
          newUser.setPassword(passwordEncoder.encode(request.getPassword()));
          newUser.setRoles(Set.of(userRole));
          return userRepository.save(newUser);
        })
        .map(this::convertDto)
        .orElseThrow(() -> new AlreadyExistsException("User already exists with email: " + request.getEmail()));
  }

  @Override
  public UserDto updateUser(Long id, UpdateUserRequest request) {
    return userRepository.findById(id).map(user -> {
      user.setFirstName(request.getFirstName());
      user.setLastName(request.getLastName());
      return userRepository.save(user);
    }).map(this::convertDto).orElseThrow(() -> {
      throw new ResourceNotFoundException("User not found with id: " + id);
    });
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.findById(id).ifPresentOrElse(userRepository::delete, () -> {
      throw new ResourceNotFoundException("User not found with id: " + id);
    });
  }

  @Override
  public User getAuthenticationUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    return userRepository.findByEmail(email);
  }

}
