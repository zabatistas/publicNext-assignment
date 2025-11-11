package com.gianniskoulopoulos.order_management.model;

public enum OrderStatus {

    UNPROCESSED,
    PROCESSING,
    PROCESSED,
    SHIPPED

    // I would suggest adding more statuses like DELIVERED, CANCELLED, RETURNED etc. depending on the business requirements.

}
