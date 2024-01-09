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
    @Email
    private String email;
    @Size(min = 4, max = 15)
    private String password;
    private List<Role> roles;

}
