package com.picture.shop.service;


import com.picture.shop.controller.dto.picture.ShoppingCartPictureDto;
import com.picture.shop.controller.exception.ResourceNotFoundException;
import com.picture.shop.model.Picture;

import javax.mail.MessagingException;
import java.util.Set;

public interface ShoppingCartService {
    void addPicture(Picture picture);
    Set<ShoppingCartPictureDto> getAllPictures();
    Integer totalPrice();
    void removePicture(int id);
    void checkOut(String userEmail) throws ResourceNotFoundException, MessagingException;
}
