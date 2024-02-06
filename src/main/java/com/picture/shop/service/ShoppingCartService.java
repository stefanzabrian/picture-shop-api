package com.picture.shop.service;


import com.picture.shop.controller.dto.picture.ShoppingCartPictureDto;
import com.picture.shop.model.Picture;

import java.util.Map;
import java.util.Set;

public interface ShoppingCartService {
    void addPicture(Picture picture);
    Set<ShoppingCartPictureDto> getAllPictures();
    Integer totalPrice();
}
