package dev.stockflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import dev.stockflow.dto.CreateProductRequest;
import dev.stockflow.model.Product;
import dev.stockflow.repository.ProductRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class ProductServiceTest {

    @Test
    void shouldCreateProduct() {
        ProductRepository repository = mock(ProductRepository.class);
        ProductService service = new ProductService(repository);

        when(repository.existsBySku("HD-001")).thenReturn(false);

        when(repository.save(any(Product.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Product product = service.createProduct(new CreateProductRequest("HD-001", "USB Headset", new BigDecimal("49.90"), 5));

        assertEquals("HD-001", product.getSku());
        assertEquals("USB Headset", product.getName());
        assertEquals(new BigDecimal("49.90"), product.getPrice());
        assertEquals(5, product.getStockQuantity());

        verify(repository).save(product);
    }

    @Test  
    void shouldRejectDuplicateSku() {
        ProductRepository repository = mock(ProductRepository.class);
        ProductService service = new ProductService(repository);

        CreateProductRequest request = new CreateProductRequest("KB-001", "USB Headset", new BigDecimal("49.90"), 5);

        when(repository.existsBySku("KB-001")).thenReturn(true);

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class, 
            () -> service.createProduct(request)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        
        verify(repository, never()).save(any(Product.class));
    }
}