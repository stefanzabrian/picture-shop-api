package com.picture.shop.service;

import com.picture.shop.controller.dto.RegisterDto;
import com.picture.shop.model.Role;
import com.picture.shop.model.User;
import com.picture.shop.repository.UserRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public UserServiceImpl
            (
                    UserRepository userRepository,
                    BCryptPasswordEncoder passwordEncoder,
                    EntityManager entityManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        this.entityManager = entityManager;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
        return new org.springframework.security.core.userdetails.User
                (
                        user.getEmail(),
                        user.getPassword(),
                        mapRolesToAuthorities(user.getRoles())
                );
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void create(RegisterDto newUser) {
        if (newUser.getEmail().isEmpty() && newUser.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email must not be Empty or Blank!");
        }
        if (newUser.getPassword().isEmpty() && newUser.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password must not be Empty or Blank!");
        }
        if (newUser.getRoles().isEmpty()) {
            throw new IllegalArgumentException("Roles must not be Empty");
        }
        User user = new User
                (
                        newUser.getEmail(),
                        passwordEncoder.encode(newUser.getPassword())
                );
        user.setRoles(newUser.getRoles());

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        User userToDelete = entityManager.find(User.class, userId);

        if(userToDelete !=null){
            userToDelete.getRoles().clear();
            entityManager.remove(userToDelete);
        } else {
            throw new EntityNotFoundException(String.format("User with id %d not found", userId));
        }
    }
}
