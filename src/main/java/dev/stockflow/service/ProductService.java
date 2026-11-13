package dev.stockflow.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dev.stockflow.model.Product;
import dev.stockflow.repository.ProductRepository;

import dev.stockflow.dto.CreateProductRequest;

@Service 
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    
    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Product createProduct(CreateProductRequest request){
        if(productRepository.existsBySku(request.sku())){
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Product already exists with SKU: " + request.sku()
            );
        }
        Product product = new Product(null, request.sku(), request.name(), request.price(), request.stockQuantity());

        return productRepository.save(product);
    }
}
