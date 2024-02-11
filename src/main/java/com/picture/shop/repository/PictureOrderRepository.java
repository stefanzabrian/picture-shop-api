package com.picture.shop.repository;

import com.picture.shop.model.PictureOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PictureOrderRepository extends JpaRepository<PictureOrder, Integer> {
    List<PictureOrder> findPictureOrderByOrder_Id(int id);
}
