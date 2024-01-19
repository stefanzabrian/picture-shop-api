package com.picture.shop.controller;

import com.picture.shop.controller.dto.auth.AuthResponseDto;
import com.picture.shop.controller.dto.login.LoginDto;
import com.picture.shop.security.jwt.JwtGenerator;
import com.picture.shop.security.jwt.SecurityConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;

    @Autowired
    public LoginController(
            AuthenticationManager authenticationManager,
            JwtGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }

    @RequestMapping("/login")
    public ResponseEntity<?> login(
           @Valid @RequestBody LoginDto loginDto
    ) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            AuthResponseDto authResponseDto = new AuthResponseDto(token);
            Date currentDate = new Date();
            authResponseDto.setExpirationDate(new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION).toString());
            return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
