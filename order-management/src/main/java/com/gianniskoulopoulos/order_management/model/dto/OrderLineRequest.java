package com.gianniskoulopoulos.order_management.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Order line item details")
public record OrderLineRequest(
    @Schema(description = "ID of the product", example = "100", required = true)
    Long productId,
    
    @Schema(description = "Quantity of the product", example = "5", required = true, minimum = "1")
    Integer quantity
) {
    
    //TODO: Validate fields -> Decide if we need custom validator

    
}
