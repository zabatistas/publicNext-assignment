package com.gianniskoulopoulos.order_management.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Order line item details")
public record OrderLineRequest(
    @Schema(description = "ID of the product", example = "100", required = true)
    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be a positive number")
    Long productId,
    
    @Schema(description = "Quantity of the product", example = "5", required = true, minimum = "1")
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    Integer quantity
) {
    
}
