package com.blpsteam.blpslab1.dto;

import com.blpsteam.blpslab1.data.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String username;
    private String password;
    private Role role;
}
