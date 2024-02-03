package com.picture.shop.controller.dto.scart;

import com.picture.shop.controller.dto.picture.PictureDto;
import com.picture.shop.model.Picture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShoppingCartDto {
    private Map<PictureDto, Integer> products;
    private Integer totalPrice;
    private LocalDateTime deliveryStart;
    private LocalDateTime deliveryEnd;
}
