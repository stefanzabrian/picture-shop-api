package com.picture.shop.controller.user;

import com.picture.shop.controller.dto.register.RegisterDto;
import com.picture.shop.model.Role;
import com.picture.shop.service.RoleService;
import com.picture.shop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final RoleService roleService;

    @Autowired
    public RegisterController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        if (userService.findByEmail(registerDto.getEmail().trim()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }
        RegisterDto newUser = new RegisterDto();
        newUser.setEmail(registerDto.getEmail().trim());
        newUser.setPassword(registerDto.getPassword().trim());

        if (roleService.findByName("USER").isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User role not found in DataBase");
        }
        Role role = roleService.findByName("USER").get();
        newUser.setRoles(Collections.singletonList(role));

        try {
            userService.create(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/moderator")
    public ResponseEntity<?> registerModerator(@Valid @RequestBody RegisterDto registerDto) {
        if (userService.findByEmail(registerDto.getEmail().trim()).isPresent()) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.CONFLICT);
        }

        RegisterDto newUser = new RegisterDto();
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(registerDto.getPassword());

        if (roleService.findByName("MODERATOR").isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Moderator role not found in DataBase");
        }
        Role roles = roleService.findByName("MODERATOR").get();
        newUser.setRoles(Collections.singletonList(roles));

        try {
            userService.create(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return new ResponseEntity<>("Moderator registered successfully!", HttpStatus.CREATED);
    }

    @PostMapping("/admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterDto registerDto) {
        if (userService.findByEmail(registerDto.getEmail().trim()).isPresent()) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.CONFLICT);
        }

        RegisterDto newUser = new RegisterDto();
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(registerDto.getPassword());

        if (roleService.findByName("ADMIN").isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Admin role not found in DataBase");
        }
        Role roles = roleService.findByName("ADMIN").get();
        newUser.setRoles(Collections.singletonList(roles));

        try {
            userService.create(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return new ResponseEntity<>("Admin registered successfully!", HttpStatus.CREATED);
    }
}
