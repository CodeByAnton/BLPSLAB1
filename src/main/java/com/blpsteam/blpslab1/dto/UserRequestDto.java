package com.blpsteam.blpslab1.dto;

import com.blpsteam.blpslab1.data.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


public record UserRequestDto(String username, String password, Role role) {
}
