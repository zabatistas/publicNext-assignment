package com.gianniskoulopoulos.order_management.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Validation error details")
public record ValidationError(
    
    @Schema(description = "Name of the field that failed validation", example = "customerId")
    String field,
    
    @Schema(description = "The rejected value", example = "null")
    Object rejectedValue,
    
    @Schema(description = "Validation error message", example = "Customer ID is required")
    String message
) {
}
