package com.gianniskoulopoulos.order_management.model;

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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Assuming there's a customerId field to link orders to customers
    private Long customerId;

    private OrderStatus status;

    // TODO: Check if more dates are needed (e.g. order modified date)
    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order")
    private List<OrderLine> orderLines;

    @Builder.Default
    private Boolean isDeleted = false;
    
}
