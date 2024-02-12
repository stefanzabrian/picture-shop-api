package com.picture.shop.service;

import com.picture.shop.controller.dto.order.OrderDto;
import com.picture.shop.controller.dto.order.SingleOrderDto;
import com.picture.shop.controller.dto.picture.PictureDto;
import com.picture.shop.controller.exception.ResourceNotFoundException;
import com.picture.shop.model.Client;
import com.picture.shop.model.Order;
import com.picture.shop.model.PictureOrder;
import com.picture.shop.model.User;
import com.picture.shop.repository.OrderRepository;
import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.NoSuchFileException;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final PictureOrderService pictureOrderService;
    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final UserService userService;

    @Autowired
    public OrderServiceImpl(PictureOrderService pictureOrderService, OrderRepository orderRepository, ClientService clientService, UserService userService) {
        this.pictureOrderService = pictureOrderService;
        this.orderRepository = orderRepository;
        this.clientService = clientService;
        this.userService = userService;
    }

    @Override
    public List<OrderDto> findAll() {
        List<OrderDto> newList = new ArrayList<>();
        for (Order order :
                orderRepository.findAll()) {
            addOrderToListOfOrderDto(newList, order);
        }
        return newList;
    }

    @Override
    public List<OrderDto> findByClientId(int id) {
        List<OrderDto> newList = new ArrayList<>();
        for (Order order :
                orderRepository.findAllByClient_Id(id)) {
            addOrderToListOfOrderDto(newList, order);
        }
        return newList;
    }

    @Override
    public Optional<Order> findById(int id) {
        return orderRepository.findById(id);
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

    @Override
    public SingleOrderDto getSingleOrder(int id, String email) throws ResourceNotFoundException, NoSuchFileException {
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User dont exists");
        }

        Client client = user.get().getClient();
        if (client.getLastName().isBlank() || client.getFirstName().isBlank() || client.getAddress().isBlank() || client.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("Client information not complete");
        } else {
            Optional<Order> clientOrder = orderRepository.findById(id);
            if (clientOrder.isEmpty()) {
                throw new NoSuchFileException("Client has no Order with id: " + id);
            } else {

                SingleOrderDto orderDto = new SingleOrderDto();
                orderDto.setId(clientOrder.get().getId());
                orderDto.setOrderNumber(clientOrder.get().getOrderNumber());
                orderDto.setDateOfOrder(clientOrder.get().getDateOfOrder());
                orderDto.setTotalPrice(clientOrder.get().getTotalPrice());
                orderDto.setStatus(clientOrder.get().getStatus());
                orderDto.setClient(clientOrder.get().getClient());
                orderDto.setEmail(userService.findByClient(orderDto.getClient()).getEmail());

                Set<PictureDto> pictureDtoSet = new HashSet<>();

                List<PictureOrder> pictureOrderList = pictureOrderService.findAllByOrderId(orderDto.getId());
                for (PictureOrder pictureOrder :
                        pictureOrderList) {
                    PictureDto pictureDto = new PictureDto();
                    pictureDto.setId(pictureOrder.getPicture().getId());
                    pictureDto.setName(pictureOrder.getPicture().getName());
                    pictureDto.setPrice(pictureOrder.getPicture().getPrice());
                    pictureDto.setDescription(pictureOrder.getPicture().getDescription());
                    pictureDto.setPictureUrl(pictureOrder.getPicture().getPictureUrl());
                    pictureDtoSet.add(pictureDto);
                }
                orderDto.setItems(pictureDtoSet);

                return orderDto;
            }
        }
    }

    @Override
    public void updateOrder(int id, OrderDto orderDto) throws ResourceNotFoundException {
        Order order = findById(id).orElseThrow(()-> new ResourceNotFoundException("Order don't exists"));
        order.setStatus(orderDto.getStatus());
        try {
            orderRepository.save(order);
        } catch (RuntimeException e){
            e.printStackTrace();
        }
    }

    @Override
    public OrderDto getOrderById(int id, String email) throws ResourceNotFoundException {
        Order order = findById(id).orElseThrow(() -> new ResourceNotFoundException("Order don't exists"));
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderNumber(order.getOrderNumber());
        orderDto.setDateOfOrder(order.getDateOfOrder());
        orderDto.setStatus(order.getStatus());
        orderDto.setTotalPrice(order.getTotalPrice());
        orderDto.setClient(order.getClient());
        orderDto.setEmail(email);
        return orderDto;
    }
}
