package com.picture.shop.controller.user;

import com.picture.shop.controller.dto.auth.ChangePasswordDto;
import com.picture.shop.controller.dto.auth.VerifyIdentityDto;
import com.picture.shop.security.jwt.JwtGenerator;
import com.picture.shop.service.PasswordService;
import com.picture.shop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.security.Principal;

@RestController
@RequestMapping("/user")
public class PasswordController {
    private final PasswordService passwordService;
    private final JwtGenerator jwtGenerator;
    private final UserService userService;

    @Autowired
    public PasswordController(PasswordService passwordService, JwtGenerator jwtGenerator, UserService userService) {
        this.passwordService = passwordService;
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
    }

    @PostMapping("/request-update-password")
    public ResponseEntity<?> sendMailLink(
            Principal principal,
            @Valid @RequestBody VerifyIdentityDto verifyIdentityDto) {
        if (userService.findByEmail(principal.getName()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with email not found");
        }
        if (verifyIdentityDto.getCurrentPassword().isBlank() || verifyIdentityDto.getCurrentPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password Must not empty or blank");
        }
        if (!passwordService.verifyIdentity(principal.getName(), verifyIdentityDto.getCurrentPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to verify identity due to bad credentials");
        }
        try {
            String token = passwordService.createPassTokenAndSendLink(
                    principal.getName(),
                    verifyIdentityDto.getCurrentPassword());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (MessagingException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, Principal principal, @Valid @RequestBody ChangePasswordDto changePasswordDto){
        boolean validated = jwtGenerator.validatePassToken(token);
        if (!validated) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token expired or invalid");
        }
        if (changePasswordDto.getNewPassword().isEmpty() ||
                changePasswordDto.getNewPassword().isBlank() ||
                changePasswordDto.getConfirmPassword().isEmpty() ||
                changePasswordDto.getConfirmPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password and Confirm Password must not empty or null");
        }
        if (userService.findByEmail(principal.getName()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with email: " + principal.getName() + " not found");
        }
        try {
            userService.changePassword(principal.getName(), changePasswordDto.getNewPassword());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Password changed but failed to send notification");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Password changed");

    }

}
