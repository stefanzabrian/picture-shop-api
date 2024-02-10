package com.picture.shop.service;

import javax.mail.MessagingException;

public interface PasswordService {
    boolean verifyIdentity(String email, String password);
    String createPassTokenAndSendLink(String email, String password) throws MessagingException;
}
