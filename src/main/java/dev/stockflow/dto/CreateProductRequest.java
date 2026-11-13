package dev.stockflow.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(

    @NotBlank
    @Size(max = 50) 
    String sku,

    @NotBlank 
    @Size(max = 200)
    String name,

    @NotNull 
    @Digits(integer = 10, fraction = 2)
    @DecimalMin("0.01")
    BigDecimal price,

    @Min(0)
    int stockQuantity

){
}
