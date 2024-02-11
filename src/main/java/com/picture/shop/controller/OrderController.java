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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        } catch (NoSuchFileException e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Client has no Order with id: " + id);
        }

    }
}
