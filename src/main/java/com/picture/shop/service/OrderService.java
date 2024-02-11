package com.picture.shop.service;

import com.picture.shop.controller.dto.order.OrderDto;
import com.picture.shop.controller.dto.order.SingleOrderDto;
import com.picture.shop.controller.exception.ResourceNotFoundException;
import com.picture.shop.model.Order;

import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDto> findAll();

    List<OrderDto> findByClientId(int id);

    Optional<Order> findById(int id);

    SingleOrderDto getSingleOrder(int id, String email) throws ResourceNotFoundException, NoSuchFileException;
}
