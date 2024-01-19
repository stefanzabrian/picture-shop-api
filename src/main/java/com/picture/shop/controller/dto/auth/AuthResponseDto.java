package com.picture.shop.controller.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer ";
    private String expirationDate;


    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
