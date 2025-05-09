package com.blpsteam.blpslab1.repositories.secondary;

import com.blpsteam.blpslab1.data.entities.secondary.User;
import com.blpsteam.blpslab1.data.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByRole(Role role);
}
