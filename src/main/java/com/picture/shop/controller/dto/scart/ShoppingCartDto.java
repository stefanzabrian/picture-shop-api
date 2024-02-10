package com.picture.shop.controller.dto.scart;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.picture.shop.controller.dto.picture.ShoppingCartPictureDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCartDto {
    private Set<ShoppingCartPictureDto> products;
    private Integer totalPrice;
    private LocalDateTime deliveryStart;
    private LocalDateTime deliveryEnd;

}
