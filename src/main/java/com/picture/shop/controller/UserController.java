package com.picture.shop.controller;

import com.picture.shop.controller.exception.JwtValidationException;
import com.picture.shop.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @RequestMapping("/removeUser/{id}")
    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<?> removeUser(@PathVariable("id") int userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
