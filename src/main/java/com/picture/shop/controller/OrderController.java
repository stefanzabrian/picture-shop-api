package com.picture.shop.controller;

import com.picture.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders(){
        if(orderService.findAll().isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Orders Yet!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(List.of(orderService.findAll()));
    }
}
