package com.gianniskoulopoulos.order_management.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

import com.gianniskoulopoulos.order_management.model.OrderStatus;

@Schema(description = "Request payload for updating an existing order")
public record OrderUpdateRequest(
    @Schema(description = "ID of the customer", example = "12345")
    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be a positive number")
    Long customerId,
    
    @Schema(description = "Order status")
    OrderStatus status,
    
    @Schema(description = "Updated list of order line items")
    @NotEmpty(message = "Order must contain at least one line item when updating order lines")
    @Valid
    List<OrderLineRequest> orderLines
) {}
