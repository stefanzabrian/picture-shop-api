package com.picture.shop.service;

import com.picture.shop.controller.dto.picture.PictureDto;
import com.picture.shop.controller.exception.ResourceNotFoundException;
import com.picture.shop.model.Picture;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PictureService {
    Picture create(PictureDto newPicture);
    List<Picture> findAll() throws ResourceNotFoundException;
    void delete(int id) throws ResourceNotFoundException;
    Optional<Picture> findById(int id);
    void update(PictureDto updatedPictureDto) throws ResourceNotFoundException;

}
