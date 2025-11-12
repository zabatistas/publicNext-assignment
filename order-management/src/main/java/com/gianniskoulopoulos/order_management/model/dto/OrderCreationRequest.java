package com.gianniskoulopoulos.order_management.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

@Schema(description = "Request payload for creating a new order")
public record OrderCreationRequest(
    @Schema(description = "List of order line items", required = true)
    @NotNull(message = "Order lines are required")
    @NotEmpty(message = "Order must contain at least one line item")
    @Valid
    List<OrderLineRequest> orderLines,
    
    @Schema(description = "ID of the customer placing the order", example = "12345", required = true)
    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be a positive number")
    Long customerId
) {
    
}
