package com.picture.shop.controller.dto.picture;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PictureDto extends BasePictureDto{
    private Integer id;

    public PictureDto(String name, Integer price, String description, String pictureUrl, Integer id) {
        super(name, price, description,pictureUrl);
        this.id = id;
    }
}
