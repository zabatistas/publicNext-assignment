package com.gianniskoulopoulos.order_management.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Standard error response structure")
public record ErrorResponse(
    
    @Schema(description = "Timestamp when the error occurred", example = "2025-11-12T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime timestamp,
    
    @Schema(description = "HTTP status code", example = "404")
    int status,
    
    @Schema(description = "Error type/code", example = "NOT_FOUND")
    String error,
    
    @Schema(description = "Human-readable error message", example = "Order not found with id: 123")
    String message,
    
    @Schema(description = "API path where error occurred", example = "/api/v1/orders/123")
    String path,
    
    @Schema(description = "List of validation errors (if applicable)")
    List<ValidationError> validationErrors
) {
    
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        this(timestamp, status, error, message, path, null);
    }
}
