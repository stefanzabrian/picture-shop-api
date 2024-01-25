package com.picture.shop.controller;

import com.picture.shop.controller.dto.picture.PictureDto;
import com.picture.shop.controller.exception.ResourceNotFoundException;
import com.picture.shop.model.Picture;
import com.picture.shop.service.PictureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/picture")
public class PictureController {
    private final PictureService pictureService;

    @Autowired
    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPicture(@Valid @RequestBody PictureDto pictureDto) {

        PictureDto newPicture = new PictureDto();
        newPicture.setName(pictureDto.getName());
        newPicture.setPrice(pictureDto.getPrice());
        newPicture.setDescription(pictureDto.getDescription());
        newPicture.setPictureUrl(pictureDto.getPictureUrl());

        try {
            newPicture.setId(pictureService.create(newPicture).getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newPicture);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPictures() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pictureService.findAll());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") int id) {
        try {
            pictureService.delete(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Picture with id: " + id + "deleted!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(value = "id") int id) {
        if (pictureService.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Picture with id: " + id + "not found!");
        }
        Picture picture = pictureService.findById(id).get();
        return ResponseEntity.status(HttpStatus.OK).body(picture);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<?> update(
            @PathVariable(value = "id") int id,
            @Valid @RequestBody PictureDto pictureDto
    ) {
        if (pictureService.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Picture not found with id: " + id);
        }
        PictureDto pictureToBeUpdated = new PictureDto();
        pictureToBeUpdated.setName(pictureDto.getName());
        pictureToBeUpdated.setPrice(pictureDto.getPrice());
        pictureToBeUpdated.setDescription(pictureDto.getDescription());
        pictureToBeUpdated.setPictureUrl(pictureDto.getPictureUrl());
        pictureToBeUpdated.setId(pictureDto.getId());

        try {
            pictureService.update(pictureToBeUpdated);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Picture with id: " + id + " updated successfully");
    }
}
