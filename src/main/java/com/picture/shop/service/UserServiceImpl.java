package com.picture.shop.service;

import com.picture.shop.controller.dto.register.RegisterDto;
import com.picture.shop.controller.exception.ResourceNotFoundException;
import com.picture.shop.model.Client;
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
    private final ClientService clientService;
    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public UserServiceImpl
            (UserRepository userRepository,
             BCryptPasswordEncoder passwordEncoder,
             ClientService clientService, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clientService = clientService;

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
    public int getClientIdByUserEmail(String email) throws ResourceNotFoundException {
        User user = findByEmail(email).orElseThrow( () -> new ResourceNotFoundException("User with email: " + email + " not found!"));
        return user.getClient().getId();

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
        Client client = new Client();
        clientService.save(client);
        user.setClient(client);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        User userToDelete = entityManager.find(User.class, userId);

        if (userToDelete != null) {
            userToDelete.getRoles().clear();
            entityManager.remove(userToDelete);
        } else {
            throw new EntityNotFoundException(String.format("User with id %d not found", userId));
        }
    }

    @Override
    public boolean verifyIdentity(String email, String password) {
        User user = findByEmail(email).get();
        if (user != null && passwordEncoder.matches(password, user.getPassword())){
            return true;
        }
        return false;
    }

    @Override
    public void changePassword(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow();
        try {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
