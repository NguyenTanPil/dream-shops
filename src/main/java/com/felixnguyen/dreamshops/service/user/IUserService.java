package com.felixnguyen.dreamshops.service.user;

import com.felixnguyen.dreamshops.dto.UserDto;
import com.felixnguyen.dreamshops.model.User;
import com.felixnguyen.dreamshops.request.CreateUserRequest;
import com.felixnguyen.dreamshops.request.UpdateUserRequest;

public interface IUserService {
  UserDto getUserById(Long id);

  User getOriginUserById(Long id);

  UserDto createUser(CreateUserRequest request);

  UserDto updateUser(Long id, UpdateUserRequest request);

  void deleteUser(Long id);
}
