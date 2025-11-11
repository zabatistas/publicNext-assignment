package com.gianniskoulopoulos.order_management.model.dto;

import java.util.List;

import com.gianniskoulopoulos.order_management.model.OrderStatus;

public record OrderUpdateRequest(Long customerId,OrderStatus status,List<OrderLineRequest> orderLines) {}
