package dev.stockflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.stockflow.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

    boolean existsBySku(String sku);    
}
