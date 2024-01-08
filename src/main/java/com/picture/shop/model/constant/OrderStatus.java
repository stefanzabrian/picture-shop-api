package com.picture.shop.model.constant;

public enum OrderStatus {
    PLACED("Placed"),
    IN_PROGRESS("In Progress"),
    DELIVERED("Delivered");
    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
}
