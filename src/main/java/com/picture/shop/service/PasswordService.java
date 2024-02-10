package com.picture.shop.service;

import com.picture.shop.controller.exception.ResourceNotFoundException;

import javax.mail.MessagingException;

public interface PasswordService {
    String getToken();
    void forgotPassword(String email) throws MessagingException, ResourceNotFoundException;
    void resetPassToken();
    boolean verifyIdentity(String email, String password);
    String createPassTokenAndSendLink(String email, String password) throws MessagingException;
}
