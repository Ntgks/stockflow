package stockflow.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import stockflow.dto.ProductRequest;
import stockflow.dto.ProductResponse;
import stockflow.exception.DuplicateSkuException;
import stockflow.exception.ProductNotFoundException;
import stockflow.service.ProductService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.PageImpl;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean 
    private ProductService productService;

    @Test
    void getAllProducts_whenEmpty_returns200AndEmptyPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);

        when(productService.getAllProducts(any(Pageable.class)))
            .thenReturn(Page.empty(pageable));

        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isEmpty())
            .andExpect(jsonPath("$.totalElements").value(0))
            .andExpect(jsonPath("$.totalPages").value(0));
    }

    
    @Test
    void getAllProducts_whenProductsExist_returns200AndProducts() throws Exception {
        ProductResponse product = new ProductResponse(
            1L,
            "Laptop",
            "LAP-001",
            10,
            new BigDecimal("999.99")
        );

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductResponse> productPage = new PageImpl<>(
            List.of(product),
            pageable,
            1
        );

        when(productService.getAllProducts(any(Pageable.class)))
            .thenReturn(productPage);

        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].name").value("Laptop"))
            .andExpect(jsonPath("$.content[0].sku").value("LAP-001"))
            .andExpect(jsonPath("$.content[0].quantity").value(10))
            .andExpect(jsonPath("$.content[0].price").value(999.99))
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(jsonPath("$.totalPages").value(1));
    }


    @Test
    void getProductById_whenExists_returns200AndProduct() throws Exception {

        ProductResponse product = new ProductResponse(
            1L,
            "Laptop",
            "LAP-001",
            10,
            new BigDecimal("999.99")
        );
        
        when(productService.getProductById(1L))
            .thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Laptop"))
            .andExpect(jsonPath("$.sku").value("LAP-001"))
            .andExpect(jsonPath("$.quantity").value(10))
            .andExpect(jsonPath("$.price").value(999.99));
    }

    @Test
    void getProductById_whenNotFound_returns404() throws Exception {
        when(productService.getProductById(999L))
            .thenThrow(new ProductNotFoundException(999L));

        mockMvc.perform(get("/api/products/999"))
            .andExpect(status().isNotFound());
    }

    @Test 
    void createProduct_whenValid_returns201() throws Exception {
        ProductResponse createdProduct = new ProductResponse(
            1L,
            "Mouse",
            "MOU-001",
            15,
            new BigDecimal("29.99")
        );

        when(productService.createProduct(any(ProductRequest.class)))
            .thenReturn(createdProduct);

        mockMvc.perform(post("/api/products")
            .contentType("application/json")
            .content("""
                {
                    "name": "Mouse",
                    "sku": "MOU-001",
                    "quantity": 15,
                    "price": 29.99
                }
                """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Mouse"))
            .andExpect(jsonPath("$.sku").value("MOU-001"))
            .andExpect(jsonPath("$.quantity").value(15))
            .andExpect(jsonPath("$.price").value(29.99));
    }

    @Test
    void createProduct_whenNameIsBlank_returns400() throws Exception {

        mockMvc.perform(post("/api/products")
                .contentType("application/json")
                .content("""
                    {
                        "name": "",
                        "sku": "MOU-002",
                        "quantity": 15,
                        "price": 29.99
                    }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProduct_whenSkuAlreadyExists_returns409() throws Exception {

        // Arrange
        when(productService.createProduct(any(ProductRequest.class)))
                .thenThrow(new DuplicateSkuException("MOU-001"));

        // Act & Assert
        mockMvc.perform(post("/api/products")
                .contentType("application/json")
                .content("""
                    {
                        "name": "Mouse",
                        "sku": "MOU-001",
                        "quantity": 15,
                        "price": 29.99
                    }
                    """))
                .andExpect(status().isConflict());
    }

    @Test
    void updateProduct_whenExists_returns200AndUpdatedProduct() throws Exception {

        // Arrange
        ProductResponse updatedProduct = new ProductResponse(
                1L,
                "Gaming Mouse",
                "MOU-001",
                20,
                new BigDecimal("39.99")
        );

        when(productService.updateProduct(
                org.mockito.ArgumentMatchers.eq(1L),
                any(ProductRequest.class)))
                .thenReturn(updatedProduct);

        // Act & Assert
        mockMvc.perform(put("/api/products/1")
                .contentType("application/json")
                .content("""
                    {
                        "name": "Gaming Mouse",
                        "sku": "MOU-001",
                        "quantity": 20,
                        "price": 39.99
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Gaming Mouse"))
                .andExpect(jsonPath("$.sku").value("MOU-001"))
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.price").value(39.99));
    }

    @Test
    void updateProduct_whenNotFound_returns404() throws Exception {

        // Arrange
        when(productService.updateProduct(
                org.mockito.ArgumentMatchers.eq(999L),
                any(ProductRequest.class)))
                .thenThrow(new ProductNotFoundException(999L));

        // Act & Assert
        mockMvc.perform(put("/api/products/999")
                .contentType("application/json")
                .content("""
                    {
                        "name": "Gaming Mouse",
                        "sku": "MOU-001",
                        "quantity": 20,
                        "price": 39.99
                    }
                    """))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProduct_whenSkuAlreadyExists_returns409() throws Exception {

        // Arrange
        when(productService.updateProduct(
                org.mockito.ArgumentMatchers.eq(1L),
                any(ProductRequest.class)))
                .thenThrow(new DuplicateSkuException("MOU-001"));

        // Act & Assert
        mockMvc.perform(put("/api/products/1")
                .contentType("application/json")
                .content("""
                    {
                        "name": "Gaming Mouse",
                        "sku": "MOU-001",
                        "quantity": 20,
                        "price": 39.99
                    }
                    """))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteProduct_whenExists_returns204() throws Exception {

        // Act & Assert
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        // Verify
        verify(productService).deleteProduct(1L);
    }

    @Test
    void deleteProduct_whenNotFound_returns404() throws Exception {

        // Arrange
        org.mockito.Mockito.doThrow(new ProductNotFoundException(999L))
                .when(productService)
                .deleteProduct(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/products/999"))
                .andExpect(status().isNotFound());

        // Verify
        verify(productService).deleteProduct(999L);
    }
}