package com.picture.shop.service;

import com.picture.shop.model.PictureOrder;
import com.picture.shop.repository.PictureOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PictureOrderServiceImpl implements PictureOrderService {
    private final PictureOrderRepository pictureOrderRepository;

    @Autowired
    public PictureOrderServiceImpl(PictureOrderRepository pictureOrderRepository) {
        this.pictureOrderRepository = pictureOrderRepository;
    }

    @Override
    public List<PictureOrder> findAllByOrderId(int id) {
        return pictureOrderRepository.findPictureOrderByOrder_Id(id);
    }
}
