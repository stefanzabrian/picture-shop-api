package com.picture.shop.service;

import com.picture.shop.controller.dto.RegisterDto;
import com.picture.shop.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<User> findByEmail(String email);

    void create(RegisterDto newUser);
    void deleteUser(int userId);
}
