package com.gianniskoulopoulos.order_management.service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.gianniskoulopoulos.order_management.model.Order;
import com.gianniskoulopoulos.order_management.model.OrderStatus;
import com.gianniskoulopoulos.order_management.model.event.OrderEvent;
import com.gianniskoulopoulos.order_management.repository.OrderEventRepository;

@Service
public class OrderEventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderEventPublisher.class);
    private static final String ORDER_EVENTS_TOPIC = "order-events";
    
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final OrderEventRepository orderEventRepository;
    private final Executor virtualThreadExecutor;
    
    public OrderEventPublisher(KafkaTemplate<String, OrderEvent> kafkaTemplate,
                              OrderEventRepository orderEventRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderEventRepository = orderEventRepository;
        // Create an executor that uses virtual threads for each task
        this.virtualThreadExecutor = task -> Thread.startVirtualThread(task);
    }
    
    /**
     * Publishes order status change event to both Kafka and MongoDB using virtual threads.
     */
    public void publishOrderStatusChangeEvent(Order order, 
                                              OrderStatus previousStatus, 
                                              OrderStatus newStatus) {
        logger.info("Publishing order status change event for order {}", order.getId());
        
        OrderEvent event = OrderEvent.builder()
                .orderId(order.getId())
                .customerId(order.getCustomerId())
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .eventTimestamp(LocalDateTime.now())
                .eventType("STATUS_CHANGED")
                .description(String.format("Order status changed from %s to %s", previousStatus, newStatus))
                .build();
        
        // Use virtual threads to publish to Kafka and save to MongoDB in parallel
        CompletableFuture<Void> kafkaPublish = CompletableFuture.runAsync(() -> {
            try {
                logger.info("Publishing to Kafka on virtual thread: {}", Thread.currentThread());
                kafkaTemplate.send(ORDER_EVENTS_TOPIC, order.getId().toString(), event)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            logger.info("Successfully published event to Kafka for order {}: {} -> {}", 
                                       order.getId(), previousStatus, newStatus);
                        } else {
                            logger.error("Failed to publish event to Kafka for order {}", 
                                        order.getId(), ex);
                        }
                    });
            } catch (Exception e) {
                logger.error("Error publishing to Kafka", e);
            }
        }, virtualThreadExecutor);
        
        CompletableFuture<Void> mongoSave = CompletableFuture.runAsync(() -> {
            try {
                logger.info("Saving to MongoDB on virtual thread: {}", Thread.currentThread());
                orderEventRepository.save(event);
                logger.info("Successfully saved event to MongoDB for order {}: {} -> {}", 
                           order.getId(), previousStatus, newStatus);
            } catch (Exception e) {
                logger.error("Error saving to MongoDB", e);
            }
        }, virtualThreadExecutor);
        
        // Both operations complete independently (fire and forget)
        CompletableFuture.allOf(kafkaPublish, mongoSave)
            .exceptionally(ex -> {
                logger.error("Error in event publishing for order {}", order.getId(), ex);
                return null;
            });
    }
}
