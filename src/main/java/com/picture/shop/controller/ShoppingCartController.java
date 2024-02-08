package com.picture.shop.controller;

import com.picture.shop.controller.dto.picture.PictureDto;
import com.picture.shop.controller.dto.picture.ShoppingCartPictureDto;
import com.picture.shop.controller.dto.scart.ShoppingCartDto;
import com.picture.shop.controller.exception.ResourceNotFoundException;
import com.picture.shop.model.Picture;
import com.picture.shop.service.PictureService;
import com.picture.shop.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;
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
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/shopping-cart")
    @ResponseBody
    public ResponseEntity<?> showShoppingCart() {
        try {
            ShoppingCartDto shoppingCartDto = new ShoppingCartDto(
                    shoppingCartService.getAllPictures(),
                    shoppingCartService.totalPrice(),
                    LocalDateTime.now().plusHours(24),
                    LocalDateTime.now().plusHours(92)
            );

            if (shoppingCartService.getAllPictures().isEmpty()) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("No Pictures in the shopping cart");
            }
            return ResponseEntity.status(HttpStatus.OK).body(shoppingCartDto);
        }catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("All pictures removed in the shopping cart");
        }
    }

    @PostMapping("/shopping-cart-remove/{id}")
    public ResponseEntity<?> removePictureFromShoppingCart(@PathVariable(value = "id") int id) {
        Optional<Picture> optionalPicture = pictureService.findById(id);
        try {
            if (optionalPicture.isPresent()) {
                shoppingCartService.removePicture(id);
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove picture");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Picture removed from cart");
    }

    @GetMapping("/cart-checkout")
    public ResponseEntity<?> checkOutCart(Principal principal) {
        try {
            System.out.println(principal.getName().trim());
            shoppingCartService.checkOut(principal.getName().trim());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Order Placed!");
    }
}
