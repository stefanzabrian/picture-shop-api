package com.picture.shop.controller;

import com.picture.shop.controller.dto.picture.PictureDto;
import com.picture.shop.service.PictureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

        try {
            newPicture.setId(pictureService.create(newPicture).getId());
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(newPicture);
    }
}
