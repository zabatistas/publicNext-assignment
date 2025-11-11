package com.gianniskoulopoulos.order_management.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import com.gianniskoulopoulos.order_management.model.OrderStatus;

@Schema(description = "Request payload for updating an existing order")
public record OrderUpdateRequest(
    @Schema(description = "ID of the customer", example = "12345")
    Long customerId,
    
    @Schema(description = "Order status")
    OrderStatus status,
    
    @Schema(description = "Updated list of order line items")
    List<OrderLineRequest> orderLines
) {}
