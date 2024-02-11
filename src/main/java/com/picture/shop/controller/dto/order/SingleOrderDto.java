package com.picture.shop.controller.dto.order;

import com.picture.shop.controller.dto.picture.PictureDto;
import com.picture.shop.model.Client;
import com.picture.shop.model.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SingleOrderDto extends OrderDto{
    private Set<PictureDto> items;

    public SingleOrderDto(Integer id, String orderNumber, Date dateOfOrder, OrderStatus status, Integer totalPrice, Client client, String email, Set<PictureDto> items) {
        super(id, orderNumber, dateOfOrder, status, totalPrice, client, email);
        this.items = items;
    }
}
