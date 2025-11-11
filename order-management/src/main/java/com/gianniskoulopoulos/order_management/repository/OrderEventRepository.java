package com.gianniskoulopoulos.order_management.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gianniskoulopoulos.order_management.model.event.OrderEvent;

import java.util.List;

@Repository
public interface OrderEventRepository extends MongoRepository<OrderEvent, String> {
    
    List<OrderEvent> findByOrderId(Long orderId);
    
    List<OrderEvent> findByCustomerId(Long customerId);
}
