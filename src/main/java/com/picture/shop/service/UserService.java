package com.picture.shop.service;

import com.picture.shop.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void create(User newUser);
}
