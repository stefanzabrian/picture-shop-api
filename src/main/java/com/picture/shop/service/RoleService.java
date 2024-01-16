package com.picture.shop.service;

import com.picture.shop.model.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);
}
