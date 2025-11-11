package com.gianniskoulopoulos.order_management.model.dto;

public record OrderLineRequest(Long productId, Integer quantity) {
    
    //TODO: Validate fields -> Decide if we need custom validator

    
}
