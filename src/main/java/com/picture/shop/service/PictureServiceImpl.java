package com.picture.shop.service;

import com.picture.shop.controller.dto.picture.PictureDto;
import com.picture.shop.controller.exception.ResourceNotFoundException;
import com.picture.shop.model.Picture;
import com.picture.shop.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

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
        if (newPicture.getPictureUrl().isBlank() && newPicture.getPictureUrl().isEmpty()) {
            throw new IllegalArgumentException("picture url must not be empty or blank");
        }

        Picture picture = new Picture();
        picture.setName(newPicture.getName());
        picture.setPrice(newPicture.getPrice());
        picture.setDescription(newPicture.getDescription());
        picture.setPictureUrl(newPicture.getPictureUrl());

        try {
            pictureRepository.save(picture);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return picture;
    }

    @Override
    public List<Picture> findAll() throws ResourceNotFoundException {
        if (pictureRepository.findAll().isEmpty()) {
            throw new ResourceNotFoundException("No Pictures yet");
        }
        return pictureRepository.findAll();
    }

    @Override
    public void delete(int id) throws ResourceNotFoundException {
        pictureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Picture with id: " + id + "not found"));
        pictureRepository.deleteById(id);
    }

    @Override
    public Optional<Picture> findById(int id) {
        return pictureRepository.findById(id);
    }

    @Override
    public void update(PictureDto updatedPictureDto) throws ResourceNotFoundException {
        Picture pictureToBeUpdated = pictureRepository.findById(updatedPictureDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Picture does not exists with id: " + updatedPictureDto.getId()));

        if (updatedPictureDto.getName().isEmpty() ||
                        updatedPictureDto.getDescription().isEmpty() ||
                        updatedPictureDto.getPictureUrl().isEmpty() ||
                updatedPictureDto.getPrice() == 0
        ) {
            throw new IllegalArgumentException("name or description or picture url must not be empty or price must not be 0");
        }
        pictureToBeUpdated.setName(updatedPictureDto.getName());
        pictureToBeUpdated.setPrice(updatedPictureDto.getPrice());
        pictureToBeUpdated.setDescription(updatedPictureDto.getDescription());
        pictureToBeUpdated.setPictureUrl(updatedPictureDto.getPictureUrl());

        try {
            pictureRepository.save(pictureToBeUpdated);
        } catch (Exception e){
            throw new RuntimeException("Error updating the picture", e);
        }
    }
}
