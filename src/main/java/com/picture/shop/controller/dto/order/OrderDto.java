package com.picture.shop.controller.dto.order;

import com.picture.shop.model.Client;
import com.picture.shop.model.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDto {
    private Integer id;
    private String orderNumber;
    private Date dateOfOrder;
    private OrderStatus status;
    private Integer totalPrice;
    private Client client;
    private String email;
}
