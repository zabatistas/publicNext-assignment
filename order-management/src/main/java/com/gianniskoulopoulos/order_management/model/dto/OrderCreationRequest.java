package com.gianniskoulopoulos.order_management.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Request payload for creating a new order")
public record OrderCreationRequest(
    @Schema(description = "List of order line items", required = true)
    List<OrderLineRequest> orderLines,
    
    @Schema(description = "ID of the customer placing the order", example = "12345", required = true)
    Long customerId
) {
    
}
