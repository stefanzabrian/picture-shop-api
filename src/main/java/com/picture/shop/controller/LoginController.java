package com.picture.shop.controller;

import com.picture.shop.controller.dto.AuthResponseDto;
import com.picture.shop.controller.dto.LoginDto;
import com.picture.shop.repository.RoleRepository;
import com.picture.shop.security.jwt.JwtGenerator;
import com.picture.shop.service.UserService;
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

@RestController
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final JwtGenerator jwtGenerator;

    @Autowired
    public LoginController(
            AuthenticationManager authenticationManager,
            RoleRepository roleRepository,
            UserService userService,
            JwtGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
    }

    @RequestMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @RequestBody LoginDto loginDto
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token) , HttpStatus.OK);
    }
}
