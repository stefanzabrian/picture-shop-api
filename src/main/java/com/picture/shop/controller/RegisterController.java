package com.picture.shop.controller;

import com.picture.shop.controller.dto.RegisterDto;
import com.picture.shop.model.Role;
import com.picture.shop.repository.RoleRepository;
import com.picture.shop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/register")
@Validated
public class RegisterController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public RegisterController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterDto registerDto) {
        if (userService.findByEmail(registerDto.getEmail()).isPresent()) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }
        RegisterDto newUser = new RegisterDto();
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(registerDto.getPassword());

        if (roleRepository.findByName("USER").isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User role not found in DataBase");
        }
        Role role = roleRepository.findByName("USER").get();
        newUser.setRoles(Collections.singletonList(role));

        try {
            userService.create(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}
