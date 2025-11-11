package com.gianniskoulopoulos.order_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gianniskoulopoulos.order_management.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
