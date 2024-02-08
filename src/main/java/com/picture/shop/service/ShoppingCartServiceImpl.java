package com.picture.shop.service;

import com.picture.shop.controller.dto.picture.ShoppingCartPictureDto;
import com.picture.shop.controller.exception.ResourceNotFoundException;
import com.picture.shop.model.*;
import com.picture.shop.model.constant.OrderStatus;
import com.picture.shop.repository.OrderRepository;
import com.picture.shop.repository.PictureOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private static final String ORDER_PREFIX = "PIC-ORD-";
    private final Map<ShoppingCartPictureDto, Integer> cart = new LinkedHashMap<>();
    private final UserService userService;
    private final PictureOrderRepository pictureOrderRepository;
    private final OrderRepository orderRepository;
    private final MailService mailService;
    private final PictureService pictureService;

    @Autowired
    public ShoppingCartServiceImpl(UserService userService, PictureOrderRepository pictureOrderRepository, OrderRepository orderRepository, MailService mailService, PictureService pictureService) {
        this.userService = userService;
        this.pictureOrderRepository = pictureOrderRepository;
        this.orderRepository = orderRepository;
        this.mailService = mailService;
        this.pictureService = pictureService;
    }

    @Override
    public void addPicture(Picture picture) {
        ShoppingCartPictureDto pictureDto = new ShoppingCartPictureDto(
                picture.getName(),
                picture.getPrice(),
                picture.getDescription(),
                picture.getPictureUrl(),
                picture.getId(),
                0);

        if (cart.containsKey(pictureDto)) {
            cart.put(pictureDto, cart.get(pictureDto) + 1);
        } else {
            cart.put(pictureDto, 1);
        }

    }

    public Set<ShoppingCartPictureDto> getAllPictures() {
        Set<ShoppingCartPictureDto> setCart = new HashSet<>();

        for (Map.Entry<ShoppingCartPictureDto, Integer> entry : cart.entrySet()) {
            ShoppingCartPictureDto pictureDto = entry.getKey();
            int quantity = entry.getValue();

            // Create a new ShoppingCartPictureDto instance with the quantity set
            ShoppingCartPictureDto updatedPictureDto = new ShoppingCartPictureDto(
                    pictureDto.getName(),
                    pictureDto.getPrice(),
                    pictureDto.getDescription(),
                    pictureDto.getPictureUrl(),
                    pictureDto.getId(),
                    quantity);

            // Add the updated pictureDto to the set
            setCart.add(updatedPictureDto);
        }

        return Collections.unmodifiableSet(setCart);
    }

    @Override
    public Integer totalPrice() {
        int totalPrice = 0;
        for (Map.Entry<ShoppingCartPictureDto, Integer> entry : cart.entrySet()) {
            totalPrice += entry.getValue() * entry.getKey().getPrice();
        }
        return totalPrice;
    }

    @Override
    public void removePicture(int id) {
        Iterator<Map.Entry<ShoppingCartPictureDto, Integer>> iterator = cart.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ShoppingCartPictureDto, Integer> entry = iterator.next();
            if (entry.getKey().getId() == id) {
                if (entry.getKey().getQuantity() == 1) {
                    iterator.remove(); // Use iterator's remove method
                } else {
                    cart.put(entry.getKey(), cart.get(entry.getKey()) - 1);
                }
            }
        }
    }

    @Override
    public void checkOut(String userEmail) throws ResourceNotFoundException {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));
        Client userProfile = user.getClient();
        if(userProfile == null) {
            throw new ResourceNotFoundException(("User profile does not exists"));
        }

        Order newOrder = new Order();
        newOrder.setOrderNumber((ORDER_PREFIX + new Random().nextInt(100000)));
        newOrder.setDateOfOrder(new Date());
        newOrder.setStatus(OrderStatus.PLACED);
        newOrder.setTotalPrice(totalPrice());
        newOrder.setClient(userProfile);
        orderRepository.save(newOrder);

      for(Map.Entry<ShoppingCartPictureDto, Integer> entry : cart.entrySet()) {
          PictureOrder pictureOrder = new PictureOrder();
          pictureOrder.setOrder(newOrder);
          Optional<Picture> picture = pictureService.findById(entry.getKey().getId());
          picture.ifPresent(
                  pictureOrder::setPicture);
          pictureOrder.setQuantity(entry.getValue());
          pictureOrderRepository.save(pictureOrder);
          cart.clear();
      }
    }
}
