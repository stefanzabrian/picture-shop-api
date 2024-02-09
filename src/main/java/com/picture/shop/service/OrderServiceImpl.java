package com.picture.shop.service;

import com.picture.shop.controller.dto.order.OrderDto;
import com.picture.shop.model.Order;
import com.picture.shop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @Override
    public List<OrderDto> findAll() {
        List<OrderDto> newList = new ArrayList<>();
        for ( Order order:
             orderRepository.findAll()) {
            addOrderToListOfOrderDto(newList, order);
        }
        return newList;
    }

    public List<OrderDto> findByClientId(int id){
        List<OrderDto> newList = new ArrayList<>();
        for ( Order order:
                orderRepository.findAllByClient_Id(id)) {
            addOrderToListOfOrderDto(newList, order);
        }
        return newList;
    }

    private void addOrderToListOfOrderDto(List<OrderDto> newList, Order order) {
        OrderDto newOrder = new OrderDto();
        newOrder.setId(order.getId());
        newOrder.setOrderNumber(order.getOrderNumber());
        newOrder.setDateOfOrder(order.getDateOfOrder());
        newOrder.setStatus(order.getStatus());
        newOrder.setClient(order.getClient());
        newOrder.setTotalPrice(order.getTotalPrice());
        newOrder.setEmail(userService.findByClient(order.getClient()).getEmail());
        newList.add(newOrder);
    }
}
