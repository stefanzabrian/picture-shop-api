package com.picture.shop.service;

import com.picture.shop.model.User;
import com.picture.shop.security.jwt.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.MessagingException;
import java.util.Optional;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PasswordServiceImpl implements PasswordService {
    private String passToken = "";
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    @Autowired
    public PasswordServiceImpl(JwtGenerator jwtGenerator, PasswordEncoder passwordEncoder, UserService userService, AuthenticationManager authenticationManager, MailService mailService) {
        this.jwtGenerator = jwtGenerator;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.mailService = mailService;
    }

    @Override
    public boolean verifyIdentity(String email, String password) {
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public String createPassTokenAndSendLink(String email, String password) throws MessagingException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password));
        passToken = jwtGenerator.generatePassToken(authentication);
        try {
            mailService.sendEmail("picture-shop-security@gmail.com",
                    email,
                    "Password reset Link",
                    "Click the following link to reset your password : http://localhost:5173/user/reset-password?token=" + passToken);
        } catch (MessagingException e) {
            throw new MessagingException("Failed to send mail");
        }
        return passToken;
    }
}
