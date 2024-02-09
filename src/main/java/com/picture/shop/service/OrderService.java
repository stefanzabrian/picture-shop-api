package com.picture.shop.service;

import com.picture.shop.controller.dto.order.OrderDto;
import com.picture.shop.model.Order;

import java.util.List;

public interface OrderService {
    List<OrderDto> findAll();
    public List<OrderDto> findByClientId(int id);
}
