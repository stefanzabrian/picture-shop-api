package com.picture.shop.service;

import com.picture.shop.controller.dto.register.RegisterDto;
import com.picture.shop.controller.exception.ResourceNotFoundException;
import com.picture.shop.model.Client;
import com.picture.shop.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.mail.MessagingException;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    int getClientIdByUserEmail(String email) throws ResourceNotFoundException;
    Optional<User> findByEmail(String email);
    User findByClient(Client client);

    void create(RegisterDto newUser);
    void deleteUser(int userId);
    boolean verifyIdentity(String email, String password);
    void changePassword(String email, String password) throws MessagingException;
}
