package com.picture.shop.service;

import com.picture.shop.controller.dto.picture.PictureDto;
import com.picture.shop.model.Picture;

import java.util.Map;

public interface ShoppingCartService {
    void addPicture(Picture picture);
    Map<PictureDto,Integer> getAllPictures();
    Integer totalPrice();
}
