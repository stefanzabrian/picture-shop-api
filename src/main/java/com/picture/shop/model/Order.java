package com.picture.shop.model;

import com.picture.shop.model.constant.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Setter
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "order_number")
    @NotNull
    @NotBlank
    private Integer orderNumber;
    @Column(name = "date_io_order")
    @NotNull
    @NotBlank
    private Date dateOfOrder;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;
    @Column(name = "total_price")
    @NotNull
    @NotBlank
    private Integer totalPrice;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Order(Integer orderNumber, Date dateOfOrder, OrderStatus status, Integer totalPrice) {
        this.orderNumber = orderNumber;
        this.dateOfOrder = dateOfOrder;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
