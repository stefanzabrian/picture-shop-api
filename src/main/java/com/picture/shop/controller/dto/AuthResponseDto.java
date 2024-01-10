package com.picture.shop.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
