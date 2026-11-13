package dev.stockflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import dev.stockflow.dto.CreateProductRequest;
import dev.stockflow.model.Product;

class ProductServiceTest {

    @Test
    void shouldCreateAndStoreProduct() {
        ProductService service = new ProductService();

        Product product = service.createProduct(new CreateProductRequest("HD-001", "USB Headset", new BigDecimal("49.90"), 5));

        assertEquals("HD-001", product.getSku());
        assertEquals("USB Headset", product.getName());
        assertEquals(new BigDecimal("49.90"), product.getPrice());
        assertEquals(5, product.getStockQuantity());

        assertTrue(service.getProducts().contains(product));
    }
}