package com.picture.shop.controller;

import com.picture.shop.model.Picture;
import com.picture.shop.service.PictureService;
import com.picture.shop.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final PictureService pictureService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, PictureService pictureService) {
        this.shoppingCartService = shoppingCartService;
        this.pictureService = pictureService;
    }

    @PostMapping("/shopping-cart-add/{id}")
    public ResponseEntity<?> addPictureToShoppingCart(@PathVariable(value = "id") int id, @RequestParam String origin) {
        try {
            Optional<Picture> pictureOptional = pictureService.findById(id);
            if (pictureOptional.isPresent()) {
                shoppingCartService.addPicture(pictureOptional.get());
            }
            if (origin != null && origin.equals("shopping-cart")) {
                return ResponseEntity.status(HttpStatus.OK).body("Picture Added To Shopping cart & Redirecting to: /shopping-cart!");
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Picture added to shopping cart");
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
