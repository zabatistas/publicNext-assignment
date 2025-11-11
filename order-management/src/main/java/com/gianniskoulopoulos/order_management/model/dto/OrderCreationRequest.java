package com.gianniskoulopoulos.order_management.model.dto;

import java.util.List;

public record OrderCreationRequest(List<OrderLineRequest> orderLines, Long customerId) {
    
}
