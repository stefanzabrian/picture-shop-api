package com.picture.shop.controller;

import com.picture.shop.controller.dto.client.ClientDto;
import com.picture.shop.controller.exception.JwtValidationException;
import com.picture.shop.controller.exception.ResourceNotFoundException;
import com.picture.shop.model.Client;
import com.picture.shop.model.User;
import com.picture.shop.service.ClientService;
import com.picture.shop.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ClientService clientService;

    @Autowired
    public UserController(UserService userService, ClientService clientService) {
        this.userService = userService;
        this.clientService = clientService;
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
    @GetMapping("/view-client")
    public ResponseEntity<?> viewClientDetails(Principal principal){
        String email = principal.getName();
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with email: " + email + " not found!");
        }
        Client client = user.get().getClient();
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @PatchMapping("/edit-client")
    public ResponseEntity<?> editClient(Principal principal, @Valid @RequestBody ClientDto clientDto){
        System.out.println(clientDto);
        if (clientDto.getFirstName().isBlank() || clientDto.getLastName().isBlank() || clientDto.getAddress().isBlank() || clientDto.getPhoneNumber().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("First, Last name and address must not empty and phone number must not 0");
        }
        if (userService.findByEmail(principal.getName()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with email: " + principal.getName() + " not found");
        }

        try {
            Client client = new Client();
            client.setId(userService.getClientIdByUserEmail(principal.getName()));
            client.setFirstName(clientDto.getFirstName());
            client.setLastName(clientDto.getLastName());
            client.setAddress(clientDto.getAddress());
            client.setPhoneNumber(clientDto.getPhoneNumber());
            clientService.save(client);
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client Id not found");
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Client details updated!");
    }
}
