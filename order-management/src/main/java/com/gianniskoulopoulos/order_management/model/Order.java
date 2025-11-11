package com.gianniskoulopoulos.order_management.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
@Builder
@Schema(description = "Order entity representing a customer order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the order", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID of the customer who placed the order", example = "12345")
    private Long customerId;

    @Schema(description = "Current status of the order")
    private OrderStatus status;

    @Schema(description = "Date and time when the order was placed", example = "2025-11-11T10:30:00")
    private LocalDateTime orderDate;

    @Schema(description = "Total amount of the order", example = "99.99")
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order")
    @Schema(description = "List of order line items")
    private List<OrderLine> orderLines;

    @Builder.Default
    @Schema(description = "Soft delete flag", example = "false")
    private Boolean isDeleted = false;
    
}
