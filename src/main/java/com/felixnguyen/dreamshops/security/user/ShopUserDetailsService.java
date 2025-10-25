package com.felixnguyen.dreamshops.security.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.felixnguyen.dreamshops.model.User;
import com.felixnguyen.dreamshops.repository.UserRepository;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ShopUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = Optional.ofNullable(userRepository.findByEmail(email))
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    return ShopUserDetails.buildUserDetails(user);
  }

}
