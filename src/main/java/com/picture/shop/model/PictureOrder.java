package com.picture.shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "picture_order")
@NoArgsConstructor
@Setter
@Getter
public class PictureOrder {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "product_price")
    @NotNull
    @NotBlank
    private Integer productPrice;
    @ManyToOne
    @JoinColumn(name = "picture_id")
    private Picture picture;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public PictureOrder(Integer productPrice) {
        this.productPrice = productPrice;
    }
}
