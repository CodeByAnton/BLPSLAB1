package com.blpsteam.blpslab1.repositories;

import com.blpsteam.blpslab1.data.entities.User;
import com.blpsteam.blpslab1.data.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByRole(Role role);
}
