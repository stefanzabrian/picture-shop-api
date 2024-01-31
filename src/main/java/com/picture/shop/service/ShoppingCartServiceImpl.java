package com.picture.shop.service;

import com.picture.shop.model.Picture;
import com.picture.shop.repository.OrderRepository;
import com.picture.shop.repository.PictureOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private static final String ORDER_PREFIX = "PIC-ORD-";
    private final Map<Picture, Integer> cart = new LinkedHashMap<>();
    private final UserService userService;
    private final PictureOrderRepository pictureOrderRepository;
    private final OrderRepository orderRepository;
    private final MailService mailService;

    @Autowired
    public ShoppingCartServiceImpl(UserService userService, PictureOrderRepository pictureOrderRepository, OrderRepository orderRepository, MailService mailService) {
        this.userService = userService;
        this.pictureOrderRepository = pictureOrderRepository;
        this.orderRepository = orderRepository;
        this.mailService = mailService;
    }

    @Override
    public void addPicture(Picture picture) {
        if(cart.containsKey(picture)) {
            cart.put(picture, cart.get(picture) + 1);
        } else {
            cart.put(picture, 1);
        }
    }
}
