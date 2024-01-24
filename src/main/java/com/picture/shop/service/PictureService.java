package com.picture.shop.service;

import com.picture.shop.controller.dto.picture.PictureDto;
import com.picture.shop.controller.exception.ResourceNotFoundException;
import com.picture.shop.model.Picture;

import java.util.List;

public interface PictureService {
    Picture create(PictureDto newPicture);
    List<Picture> findAll() throws ResourceNotFoundException;
    void delete(int id) throws ResourceNotFoundException;

}
