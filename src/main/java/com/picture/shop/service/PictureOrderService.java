package com.picture.shop.service;

import com.picture.shop.model.PictureOrder;

import java.util.List;

public interface PictureOrderService {
    List<PictureOrder> findAllByOrderId(int id);
}
