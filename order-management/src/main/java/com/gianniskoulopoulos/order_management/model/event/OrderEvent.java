package com.gianniskoulopoulos.order_management.model.event;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.gianniskoulopoulos.order_management.model.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "order_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    
    @Id
    private String id;
    
    private Long orderId;
    
    private Long customerId;
    
    private OrderStatus previousStatus;
    
    private OrderStatus newStatus;
    
    private LocalDateTime eventTimestamp;
    
    private String eventType; // e.g., "STATUS_CHANGED"
    
    private String description;
}
