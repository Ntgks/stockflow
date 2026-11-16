package stockflow.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.persistence.GenerationType;

@Entity 
@Table(name = "products")
public class Product{

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Min(0)
    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;

    protected Product(){
    }

    public Product(
        String name,
        String sku,
        int quantity,
        BigDecimal price){
            this.name = name;
            this.sku = sku;
            this.quantity = quantity;
            this.price = price;
    }

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getSku(){
        return sku;
    }

    public int getQuantity(){
        return quantity;
    }

    public BigDecimal getPrice(){
        return price;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setSku(String sku){
        this.sku = sku;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price){
        this.price = price;
    }

}