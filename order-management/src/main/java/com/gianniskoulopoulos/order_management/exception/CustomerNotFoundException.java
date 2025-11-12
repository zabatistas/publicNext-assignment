package com.gianniskoulopoulos.order_management.exception;

public class CustomerNotFoundException extends RuntimeException {
    
    private final Long customerId;

    public CustomerNotFoundException(Long customerId) {
        super(String.format("Customer not found with id: %d", customerId));
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }
}
