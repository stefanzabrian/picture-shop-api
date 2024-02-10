package com.picture.shop.controller.dto.picture;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCartPictureDto extends PictureDto{
    private Integer quantity;

    public ShoppingCartPictureDto(String name, Integer price, String description, String pictureUrl, Integer id, Integer quantity) {
        super(name, price, description, pictureUrl, id);
        this.quantity = quantity;
    }
}
