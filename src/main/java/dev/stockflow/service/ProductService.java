package dev.stockflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.stockflow.model.Product;

import java.math.BigDecimal;

import java.util.ArrayList;

import dev.stockflow.dto.CreateProductRequest;

@Service 
public class ProductService {

    private final List<Product> products = new ArrayList<>(List.of(
        new Product(1L, "KB-001", "Mechanical Keyboard", new BigDecimal("79.90"), 10),
        new Product(2L, "MS-001", "Wireless Mouse", new BigDecimal("24.50"), 20)
    ));

    private long nextId = 3L;
    
    public List<Product> getProducts(){
        return List.copyOf(products);
    }

    public Product createProduct(CreateProductRequest request){
        Product product = new Product(nextId ++, request.sku(), request.name(), request.price(), request.stockQuantity());

        products.add(product);

        return product;
    }
}
