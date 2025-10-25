package com.felixnguyen.dreamshops.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.felixnguyen.dreamshops.model.Role;
import com.felixnguyen.dreamshops.repository.RoleRepository;

@Configuration
public class DataInitializer {

  @Bean
  public CommandLineRunner initRoles(RoleRepository roleRepository) {
    return args -> {
      String[] defaultRoles = { "ROLE_USER", "ROLE_ADMIN" };

      for (String roleName : defaultRoles) {
        roleRepository.findByName(roleName).ifPresentOrElse(
            role -> System.out.println("✅ Role already exists: " + roleName),
            () -> {
              roleRepository.save(new Role(roleName));
              System.out.println("✅ Created default role: " + roleName);
            });
      }
    };
  }
}
