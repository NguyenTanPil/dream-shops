package com.felixnguyen.dreamshops.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.felixnguyen.dreamshops.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);
}
