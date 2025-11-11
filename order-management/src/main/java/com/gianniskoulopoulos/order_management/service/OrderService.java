package com.gianniskoulopoulos.order_management.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gianniskoulopoulos.order_management.model.Order;
import com.gianniskoulopoulos.order_management.model.OrderStatus;
import com.gianniskoulopoulos.order_management.model.dto.OrderCreationRequest;
import com.gianniskoulopoulos.order_management.model.dto.OrderUpdateRequest;

@Service
public interface OrderService {
    
    Order createOrder(OrderCreationRequest request);
    
    Order getOrderById(Long orderId);
    
    Order updateOrder(Long orderId, OrderUpdateRequest request);
    
    void softDeleteOrder(Long orderId);
    
    Page<Order> getAllOrders(Long customerId, OrderStatus status, Pageable pageable);
    
    Order updateOrderStatus(Long orderId, OrderStatus status);
}
