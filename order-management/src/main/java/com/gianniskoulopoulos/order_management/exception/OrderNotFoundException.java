package com.gianniskoulopoulos.order_management.exception;

public class OrderNotFoundException extends RuntimeException {
    
    private final Long orderId;

    public OrderNotFoundException(Long orderId) {
        super(String.format("Order not found with id: %d", orderId));
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
