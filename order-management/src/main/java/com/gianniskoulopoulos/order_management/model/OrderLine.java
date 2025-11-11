package com.gianniskoulopoulos.order_management.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "order_line")
@Data
@Builder
public class OrderLine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Check if we need a relation to Order entity
    @ManyToOne
    private Order order;

    private Long productId;

    private Integer quantity;

    private BigDecimal pricePerUnit;
}
