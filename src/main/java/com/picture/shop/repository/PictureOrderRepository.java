package com.picture.shop.repository;

import com.picture.shop.model.PictureOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureOrderRepository extends JpaRepository<PictureOrder, Integer> {
}
