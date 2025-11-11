package com.gianniskoulopoulos.order_management.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gianniskoulopoulos.order_management.model.Order;
import com.gianniskoulopoulos.order_management.model.OrderLine;
import com.gianniskoulopoulos.order_management.model.OrderStatus;
import com.gianniskoulopoulos.order_management.model.Product;
import com.gianniskoulopoulos.order_management.model.dto.OrderCreationRequest;
import com.gianniskoulopoulos.order_management.model.dto.OrderLineRequest;
import com.gianniskoulopoulos.order_management.model.dto.OrderUpdateRequest;
import com.gianniskoulopoulos.order_management.repository.OrderRepository;
import com.gianniskoulopoulos.order_management.repository.ProductRepository;
import com.gianniskoulopoulos.order_management.service.OrderService;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order createOrder(OrderCreationRequest request) {

        
        // We will not implement this method fully now, just a stub to show the structure
        checkIfCustomerExists(request.customerId());

        // TODO: Determine what to do if one of the products does not exist or has insufficient stock -> Mostly business, should come from requirements
        for(OrderLineRequest olr : request.orderLines()) {
            Product product = productRepository.findById(olr.productId()).orElseThrow(() -> new RuntimeException());

            checkIfProductsExistAndHaveSufficientStock(product, olr.quantity());
        }

        

        return orderRepository.save(Order.builder()
            .customerId(request.customerId())
            .orderLines(request.orderLines().stream().map(olr -> 
                OrderLine.builder()
                    .productId(olr.productId())
                    .quantity(olr.quantity())
                    .build()
            ).toList())
            .build());
    }

    private void checkIfProductsExistAndHaveSufficientStock(Product product, Integer quantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkIfProductsExistAndHaveSufficientStock'");
    }

    private void checkIfCustomerExists(Long customerId) {
        throw new UnsupportedOperationException("Unimplemented method 'checkIfCustomerExists'");
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    @Override
    public Order updateOrder(Long orderId, OrderUpdateRequest request) {
        // Find the existing order
        Order existingOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // Validate customer if provided
        if (request.customerId() != null) {
            checkIfCustomerExists(request.customerId());
            existingOrder.setCustomerId(request.customerId());
        }

        // Update status if provided
        if (request.status() != null) {
            existingOrder.setStatus(request.status());
        }

        // Update order lines if provided
        if (request.orderLines() != null && !request.orderLines().isEmpty()) {
            // Validate products
            for (OrderLineRequest olr : request.orderLines()) {
                Product product = productRepository.findById(olr.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + olr.productId()));
                checkIfProductsExistAndHaveSufficientStock(product, olr.quantity());
            }

            // Update order lines
            existingOrder.setOrderLines(request.orderLines().stream()
                .map(olr -> OrderLine.builder()
                    .productId(olr.productId())
                    .quantity(olr.quantity())
                    .order(existingOrder)
                    .build())
                .toList());
        }

        // Save and return updated order
        return orderRepository.save(existingOrder);
    }

    @Override
    public void softDeleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        order.setIsDeleted(true);
        orderRepository.save(order);
    }

    @Override
    public Page<Order> getAllOrders(Long customerId, OrderStatus status, Pageable pageable) {
        Specification<Order> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Filter out soft-deleted orders by default
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            
            // Filter by customerId if provided
            if (customerId != null) {
                predicates.add(criteriaBuilder.equal(root.get("customerId"), customerId));
            }
            
            // Filter by status if provided
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return orderRepository.findAll(spec, pageable);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        order.setStatus(status);
        return orderRepository.save(order);
    }
    
}
