package com.picture.shop.service;

import com.picture.shop.controller.dto.picture.PictureDto;
import com.picture.shop.model.Picture;

public interface PictureService {
    Picture create(PictureDto newPicture);

}
