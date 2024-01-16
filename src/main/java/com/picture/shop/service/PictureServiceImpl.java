package com.picture.shop.service;

import com.picture.shop.controller.dto.picture.PictureDto;
import com.picture.shop.model.Picture;
import com.picture.shop.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @Override
    public Picture create(PictureDto newPicture) {
        if (newPicture.getName().isBlank() && newPicture.getName().isEmpty()) {
            throw new IllegalArgumentException("name must not be empty or blank");
        }
        if (newPicture.getPrice() == 0) {
            throw new NullPointerException("price must not be zero");
        }
        if (newPicture.getDescription().isBlank() && newPicture.getDescription().isEmpty()) {
            throw new IllegalArgumentException("description must not be empty or blank");
        }

        Picture picture = new Picture();
        picture.setName(newPicture.getName());
        picture.setPrice(newPicture.getPrice());
        picture.setDescription(newPicture.getDescription());

        try {
            pictureRepository.save(picture);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return picture;
    }
}
