package com.picture.shop.controller.dto.auth;

import com.picture.shop.utils.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@FieldMatch.List({
        @FieldMatch(first = "newPassword", second = "confirmPassword", message = "Password must match")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChangePasswordDto {
    @NotBlank
    @NotNull
    private String newPassword;
    @NotBlank
    @NotNull
    private String confirmPassword;

}
