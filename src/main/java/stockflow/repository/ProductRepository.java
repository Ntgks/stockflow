package stockflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import stockflow.model.Product;

public interface ProductRepository extends JpaRepository <Product, Long>{

    boolean existsBySku(String sku);

    
}