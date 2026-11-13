package dev.stockflow.model;

import java.math.BigDecimal;

public class Product {

    private Long id;

    private String sku;

    private String name;

    private BigDecimal price;

    private int stockQuantity;

    public Product(
        Long id,
        String sku,
        String name,
        BigDecimal price,
        int stockQuantity
    ){
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Long getId(){
        return id;
    }

    public String getSku(){
        return sku;
    }

    public String getName(){
        return name;
    }

    public BigDecimal getPrice(){
        return price;
    }

    public int getStockQuantity(){
        return stockQuantity;
    }
    
}
