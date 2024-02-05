package com.picture.shop.controller.dto.scart;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.picture.shop.controller.dto.picture.PictureDto;
import com.picture.shop.model.Picture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCartDto {
    private Map<PictureDto, Integer> products;
    private Integer totalPrice;
    private LocalDateTime deliveryStart;
    private LocalDateTime deliveryEnd;

    public Map<PictureDto, Integer> getProductsSerialized() {
        // Create a new map for serialization
        Map<PictureDto, Integer> serializedMap = new HashMap<>();

        // Iterate over the original products map and add entries to the serialized map
        for (Map.Entry<PictureDto, Integer> entry : this.products.entrySet()) {
            PictureDto pictureDto = entry.getKey();
            Integer quantity = entry.getValue();

            // Assuming PictureDto has a meaningful equals() and hashCode() implementation
            serializedMap.put(pictureDto, quantity);
        }

        return serializedMap;
    }
}
