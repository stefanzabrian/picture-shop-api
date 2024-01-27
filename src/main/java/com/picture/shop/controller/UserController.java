package com.picture.shop.controller;

import com.picture.shop.controller.exception.JwtValidationException;
import com.picture.shop.model.Client;
import com.picture.shop.model.User;
import com.picture.shop.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

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
    @GetMapping("/view-client-details")
    public ResponseEntity<?> viewClientDetails(Principal principal){
        String email = principal.getName();
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with email: " + email + " not found!");
        }
        Client client = user.get().getClient();
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }


}
