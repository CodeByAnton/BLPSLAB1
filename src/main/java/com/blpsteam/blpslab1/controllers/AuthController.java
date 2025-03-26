package com.blpsteam.blpslab1.controllers;


import com.blpsteam.blpslab1.data.entities.User;
import com.blpsteam.blpslab1.dto.UserRequestDto;
import com.blpsteam.blpslab1.service.UserService;
import com.blpsteam.blpslab1.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.blpsteam.blpslab1.exceptions.UsernameNotFoundException;
import com.blpsteam.blpslab1.exceptions.InvalidCredentialsException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtUtil;

    public AuthController(UserService userService, JwtService jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register( @RequestBody UserRequestDto userRequestDto) {
        User user = userService.registerUser(userRequestDto.username(), userRequestDto.password(), userRequestDto.role());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.format("User %s registered successfully", user.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto userRequestDto) {
        if (!userService.checkCredentials(userRequestDto.username(), userRequestDto.password())) {
            throw new InvalidCredentialsException("Wrong username or password");
        }

        return userService.findByUsername(userRequestDto.username())
                .map(user -> ResponseEntity.ok(jwtUtil.generateToken(user)))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}

