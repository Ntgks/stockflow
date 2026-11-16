package stockflow.controller;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import stockflow.dto.ProductResponse;
import stockflow.service.ProductService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean 
    private ProductService productService;

    @Test 
    void getAllProducts_whenEmpty_returns200AndEmptyJsonArray() throws Exception {
        when(productService.getAllProducts())
            .thenReturn(List.of());

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
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
        
        when(productService.getAllProducts())
            .thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(content().json("""
                [
                    {
                        "id": 1,
                        "name": "Laptop",
                        "sku": "LAP-001",
                        "quantity": 10,
                        "price": 999.99
                    }
                ]
            """));
    }

}