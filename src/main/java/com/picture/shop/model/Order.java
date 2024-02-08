package com.picture.shop.model;

import com.picture.shop.model.constant.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private String orderNumber;
    @Column(name = "date_io_order")
    @NotNull
    private Date dateOfOrder;
    @Column(name = "status")
    private OrderStatus status;
    @Column(name = "total_price")
    @NotNull
    private Integer totalPrice;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Order(String orderNumber, Date  dateOfOrder, OrderStatus status, Integer totalPrice) {
        this.orderNumber = orderNumber;
        this.dateOfOrder = dateOfOrder;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
