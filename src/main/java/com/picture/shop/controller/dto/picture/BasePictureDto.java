package com.picture.shop.controller.dto.picture;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class BasePictureDto {
    @NotNull(message = "name must be not null")
    @NotBlank(message = "name must not be blank")
    private String name;
    @NotNull(message = "price must be not null")
    private Integer price;
    @NotNull(message = "description must be not null")
    @NotBlank(message = "description must not be blank")
    private String description;
}
