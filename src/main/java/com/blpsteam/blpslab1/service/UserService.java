package com.blpsteam.blpslab1.service;


import com.blpsteam.blpslab1.data.entities.User;
import com.blpsteam.blpslab1.data.enums.Role;
import com.blpsteam.blpslab1.exceptions.AdminAlreadyExistsException;
import com.blpsteam.blpslab1.exceptions.UsernameAlreadyExistsException;
import com.blpsteam.blpslab1.exceptions.impl.UserAbsenceException;
import com.blpsteam.blpslab1.repositories.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }



    public User registerUser(String username, String password, Role role) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }


        if (role == Role.ADMIN && userRepository.existsByRole(Role.ADMIN)) {
            throw new AdminAlreadyExistsException("Admin user already exists");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }


        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }



    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public boolean checkCredentials(String username, String password) {
        return findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    public Long getUserIdFromToken(String token) {
        String username = jwtService.extractUsername(token);
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Long getUserIdFromContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .map(User::getId)
                    .orElseThrow(() -> new UserAbsenceException("Такого пользователя не существует"));
        }

        throw new UserAbsenceException("Такого пользователя не существует");
    }

    public Role getUserRoleFromContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .map(User::getRole)
                    .orElseThrow(() -> new UserAbsenceException("Такого пользователя не существует"));
        }

        throw new UserAbsenceException("Такого пользователя не существует");
    }
}
