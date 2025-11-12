package com.gianniskoulopoulos.order_management.exception;

import com.gianniskoulopoulos.order_management.model.OrderStatus;

public class InvalidOrderStatusTransitionException extends RuntimeException {
    
    private final OrderStatus currentStatus;
    private final OrderStatus newStatus;

    public InvalidOrderStatusTransitionException(OrderStatus currentStatus, OrderStatus newStatus) {
        super(String.format("Invalid status transition from %s to %s", currentStatus, newStatus));
        this.currentStatus = currentStatus;
        this.newStatus = newStatus;
    }

    public OrderStatus getCurrentStatus() {
        return currentStatus;
    }

    public OrderStatus getNewStatus() {
        return newStatus;
    }
}
