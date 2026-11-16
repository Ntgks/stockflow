package stockflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import stockflow.dto.ProductRequest;
import stockflow.dto.ProductResponse;
import stockflow.exception.DuplicateSkuException;
import stockflow.exception.ProductNotFoundException;
import stockflow.model.Product;
import stockflow.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock 
    private ProductRepository productRepository;

    @InjectMocks 
    private ProductService productService;


    @Test 
    void getProductById_whenProductDoesNotExist_throwsException(){

        when(productRepository.findById(999L))
            .thenReturn(Optional.empty());

        assertThrows(
            ProductNotFoundException.class, 
            () -> productService.getProductById(999L)
        );

    }

    @Test 
    void getProductById_whenProductExists_returnsProduct(){
        Product product = new Product(
            "Laptop",
            "LAP-001",
            10,
            new BigDecimal("999.99")
        );

        when(productRepository.findById(1L))
            .thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById(1L);

        assertEquals("Laptop", response.name());
        assertEquals("LAP-001", response.sku());
        assertEquals(10, response.quantity());
        assertEquals(new BigDecimal("999.99"), response.price());
    }

    @Test 
    void createProduct_whenSkuAlreadyExists_throwsException(){
        ProductRequest request = new ProductRequest(
            "Mouse",
            "MOUSE-001",
            20,
            new BigDecimal("25.99")
        );
        
        when(productRepository.existsBySku("MOUSE-001"))
            .thenReturn(true);

        assertThrows(
            DuplicateSkuException.class, 
            () -> productService.createProduct(request)
        );

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test 
    void createProduct_whenSkuIsAvailable_returnsProduct() {
        ProductRequest request = new ProductRequest(
            "Keyboard",
            "KEY-001",
            15,
            new BigDecimal("49.99")
        ); 
        
        Product savedProduct = new Product(
            "Keyboard",
            "KEY-001",
            15,
            new BigDecimal("49.99")
        );
        
        when(productRepository.existsBySku("KEY-001"))
            .thenReturn(false);

        when(productRepository.save(any(Product.class)))
            .thenReturn(savedProduct);

        ProductResponse response = productService.createProduct(request);

        assertEquals("Keyboard", response.name());
        assertEquals("KEY-001", response.sku());
        assertEquals(15, response.quantity());
        assertEquals(new BigDecimal("49.99"), response.price());

        verify(productRepository).save(any(Product.class));
    }

    @Test 
    void updateProduct_whenProductDoesNotExist_throwsException() {
        ProductRequest request = new ProductRequest(
            "Updated-Laptop",
            "LAP-001",
            5,
            new BigDecimal("899.99")
        );
        
        when(productRepository.findById(999L))
            .thenReturn(Optional.empty());

        assertThrows(
            ProductNotFoundException.class, 
            () -> productService.updateProduct(999L, request)
        );

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test 
    void updateProduct_whenProductExists_updatesProduct() {
        Product existingProduct = new Product(
            "Laptop",
            "LAP-001",
            10,
            new BigDecimal("999.99")
        );
        
        ProductRequest request = new ProductRequest(
            "Updated-Laptop",
            "LAP-001",
            5,
            new BigDecimal("899.99")
        );
        
        when(productRepository.findById(1L))
            .thenReturn(Optional.of(existingProduct));

        when(productRepository.save(any(Product.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse response = productService.updateProduct(1L, request);

        assertEquals("Updated-Laptop", response.name());
        assertEquals("LAP-001", response.sku());
        assertEquals(5, response.quantity());
        assertEquals(new BigDecimal("899.99"), response.price());

        verify(productRepository).save(any(Product.class));
    }

    @Test 
    void updateProduct_whenSkuBelongsToAnotherProduct_throwsException() {
        Product existingProduct = new Product(
            "Laptop",
            "LAP-001",
            10,
            new BigDecimal("999.99")
        );
        
        ProductRequest request = new ProductRequest(
            "Updated-Laptop",
            "MOUSE-001",
            5,
            new BigDecimal("899.99")
        );
        
        when(productRepository.findById(1L))
            .thenReturn(Optional.of(existingProduct));

        when(productRepository.existsBySku("MOUSE-001"))
            .thenReturn(true);

        assertThrows(
            DuplicateSkuException.class, 
            () -> productService.updateProduct(1L, request)
        );

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test 
    void deleteProduct_whenProductDoesNotExist_throwsException() {
        when(productRepository.findById(999L))
            .thenReturn(Optional.empty());

        assertThrows(
            ProductNotFoundException.class, 
            () -> productService.deleteProduct(999L)
        );

        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test 
    void deleteProduct_whenProductExists_deletesProduct() {
        Product existingProduct = new Product(
            "Laptop",
            "LAP-001",
            10,
            new BigDecimal("999.99")
        );

        when(productRepository.findById(1L))
            .thenReturn(Optional.of(existingProduct));

        productService.deleteProduct(1L);

        verify(productRepository).delete(existingProduct);
    }

    @Test 
    void getAllProducts_whenProductsExist_returnsProducts() {
        Product laptop = new Product(
            "Laptop",
            "LAP-001",
            10,
            new BigDecimal("999.99")
        );

        Product mouse = new Product(
            "Mouse",
            "MOUSE-001",
            20,
            new BigDecimal("25.99")
        );
        
        when(productRepository.findAll())
            .thenReturn(List.of(laptop, mouse));

        List<ProductResponse> response = productService.getAllProducts();

        assertEquals(2, response.size());
        assertEquals("Laptop", response.get(0).name());
        assertEquals("LAP-001", response.get(0).sku());
        assertEquals("Mouse", response.get(1).name());
        assertEquals("MOUSE-001", response.get(1).sku());

        verify(productRepository).findAll();
    }

    @Test 
    void getAllProducts_whenNoProductsExist_returnsEmptyList() {
        when(productRepository.findAll())
            .thenReturn(List.of());

        List<ProductResponse> response = productService.getAllProducts();

        assertEquals(0, response.size());
        verify(productRepository).findAll();
    }

    
}
