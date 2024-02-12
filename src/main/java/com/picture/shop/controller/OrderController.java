package com.picture.shop.controller;

import com.picture.shop.controller.dto.order.OrderDto;
import com.picture.shop.controller.dto.order.SingleOrderDto;
import com.picture.shop.controller.exception.ResourceNotFoundException;
import com.picture.shop.model.Order;
import com.picture.shop.model.User;
import com.picture.shop.service.OrderService;
import com.picture.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.NoSuchFileException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders() {
        if (orderService.findAll().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Orders Yet!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(List.of(orderService.findAll()));
    }

    @GetMapping("/user-orders")
    public ResponseEntity<?> getAllUserOrders(Principal principal) {
        Optional<User> user = userService.findByEmail(principal.getName());
        if (user.isPresent()) {
            List<OrderDto> userOrders = orderService.findByClientId(user.get().getClient().getId());
            return ResponseEntity.status(HttpStatus.OK).body(userOrders);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Orders Yet");
        }
    }

    @GetMapping("/single-order-view/{id}")
    public ResponseEntity<?> singleOrderView(@PathVariable("id") int id, Principal principal) {
        try {
            SingleOrderDto singleOrderDto = orderService.getSingleOrder(id, principal.getName());
            return ResponseEntity.status(HttpStatus.OK).body(singleOrderDto);
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User dont exists");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Client dont exists");
        } catch (NoSuchFileException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Client has no Order with id: " + id);
        }
    }
    @GetMapping("/update-order/{id}")
    public ResponseEntity<?> getUpdateOrder(@PathVariable("id") int id, Principal principal){
        if (userService.findByEmail(principal.getName()).isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User dont exists");
        }
        try {
            OrderDto orderDto = orderService.getOrderById(id, principal.getName());
            return ResponseEntity.status(HttpStatus.OK).body(orderDto);
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Order dont exists with id: " + id);
        }
    }
    @PatchMapping("/update-order/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") int id, @RequestBody OrderDto orderDto) {
        if (orderDto.getStatus() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status not completed");
        }
        try {
            orderService.updateOrder(id, orderDto);
            return ResponseEntity.status(HttpStatus.OK).body("Order Updated");
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Order dont exists");
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update the order!");
        }
    }
}
