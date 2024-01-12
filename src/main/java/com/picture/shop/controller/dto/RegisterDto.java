package com.picture.shop.controller.dto;

import com.picture.shop.model.Role;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class RegisterDto {
    @Email(message = "Email must be a well-formed email address")
    @NotNull(message = "Email must not be null")
    @NotBlank(message = "Email must not be blank")
    private String email;
    @Size(min = 4, max = 15, message = "Password size must be between 4 and 15")
    @NotNull(message = "Password must not be null")
    @NotBlank(message = "Password must not be blank")
    private String password;
    private List<Role> roles;

}
