package dev.stockflow.controller;

import org.springframework.web.bind.annotation.RestController;

import dev.stockflow.dto.CreateProductRequest;
import dev.stockflow.model.Product;
import dev.stockflow.service.ProductService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;



@RestController 
public class ProductController {
    
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/api/products")
    public List<Product> getProducts(){
        return productService.getProducts();
    }

    @PostMapping("/api/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(
        @Valid @RequestBody CreateProductRequest request){
            return productService.createProduct(request);
        }
    
    
}
