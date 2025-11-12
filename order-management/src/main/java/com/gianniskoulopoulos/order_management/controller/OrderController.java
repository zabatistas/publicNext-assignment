package com.gianniskoulopoulos.order_management.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gianniskoulopoulos.order_management.model.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import com.gianniskoulopoulos.order_management.model.OrderStatus;
import com.gianniskoulopoulos.order_management.model.dto.OrderCreationRequest;
import com.gianniskoulopoulos.order_management.model.dto.OrderUpdateRequest;
import com.gianniskoulopoulos.order_management.model.event.OrderEvent;
import com.gianniskoulopoulos.order_management.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController()
@RequestMapping("/api/v1/orders")
@Tag(name = "Order Management", description = "APIs for managing customer orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    // TODO: Change the incoming DTO to Pageable directly if possible
    @Operation(summary = "Get all orders", description = "Retrieve a paginated list of orders with optional filtering by customer ID and status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved orders",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("")
    public ResponseEntity<Page<Order>> getAllOrders(
            @Parameter(description = "Filter by customer ID") @Positive(message = "Customer ID must be a positive number") @RequestParam(required = false) Long customerId,
            @Parameter(description = "Filter by order status") @RequestParam(required = false) OrderStatus status,
            @Parameter(description = "Page number (0-indexed)") @Min(value = 0, message = "Page number must be 0 or greater") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @Min(value = 1, message = "Page size must be at least 1") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "orderDate") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)") @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Order> orders = orderService.getAllOrders(customerId, status, pageable);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Create a new order", description = "Create a new order with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("")
    public ResponseEntity<Order> postMethodName(
            @Parameter(description = "Order creation request") @Valid @RequestBody OrderCreationRequest request) throws URISyntaxException {
        

        Order createdOrder = orderService.createOrder(request);
        return ResponseEntity.created(new URI("/api/v1/orders/" + createdOrder.getId())).body(createdOrder);
    }

    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @Parameter(description = "Order ID") @Positive(message = "Order ID must be a positive number") @PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Update order", description = "Update an existing order with new details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order updated successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(
            @Parameter(description = "Order ID") @Positive(message = "Order ID must be a positive number") @PathVariable Long id,
            @Parameter(description = "Order update request") @Valid @RequestBody OrderUpdateRequest request) {
        Order updatedOrder = orderService.updateOrder(id, request);
        return ResponseEntity.ok(updatedOrder);
    }

    @Operation(summary = "Delete order", description = "Soft delete an order (marks as deleted without removing from database)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteOrder(
            @Parameter(description = "Order ID") @Positive(message = "Order ID must be a positive number") @PathVariable Long id) {
        orderService.softDeleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update order status", description = "Update only the status of an existing order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid status", content = @Content)
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @Parameter(description = "Order ID") @Positive(message = "Order ID must be a positive number") @PathVariable Long id,
            @Parameter(description = "New order status") @RequestBody OrderStatus status) {
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @Operation(summary = "Get order history", description = "Retrieve the event history for a specific order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order history retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderEvent.class))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @GetMapping("/{id}/history")
    public ResponseEntity<List<OrderEvent>> getOrderHistory(
            @Parameter(description = "Order ID") @Positive(message = "Order ID must be a positive number") @PathVariable Long id) {
        List<OrderEvent> orderHistory = orderService.getOrderHistory(id);
        return ResponseEntity.ok(orderHistory);
    }

}