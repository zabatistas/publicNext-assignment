package com.gianniskoulopoulos.order_management.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gianniskoulopoulos.order_management.model.Order;
import com.gianniskoulopoulos.order_management.model.OrderStatus;
import com.gianniskoulopoulos.order_management.model.dto.OrderCreationRequest;
import com.gianniskoulopoulos.order_management.model.dto.OrderUpdateRequest;
import com.gianniskoulopoulos.order_management.model.event.OrderEvent;
import com.gianniskoulopoulos.order_management.service.OrderService;

import java.util.List;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController()
// TODO: Add versioning to the API, in order to do that, we need to update the paths below
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public ResponseEntity<Page<Order>> getAllOrders(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Order> orders = orderService.getAllOrders(customerId, status, pageable);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("")
    public ResponseEntity<Order> postMethodName(@RequestBody OrderCreationRequest request) throws URISyntaxException {
        

        Order createdOrder = orderService.createOrder(request);
        return ResponseEntity.created(new URI("/api/v1/orders/" + createdOrder.getId())).body(createdOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody OrderUpdateRequest request) {
        Order updatedOrder = orderService.updateOrder(id, request);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteOrder(@PathVariable Long id) {
        orderService.softDeleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatus status) {
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<OrderEvent>> getOrderHistory(@PathVariable Long id) {
        List<OrderEvent> orderHistory = orderService.getOrderHistory(id);
        return ResponseEntity.ok(orderHistory);
    }

}