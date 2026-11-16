package stockflow.service;

import java.util.List;
import org.springframework.stereotype.Service;

import stockflow.dto.ProductRequest;
import stockflow.dto.ProductResponse;
import stockflow.model.Product;
import stockflow.repository.ProductRepository;

import stockflow.exception.DuplicateSkuException;
import stockflow.exception.ProductNotFoundException;
@Service 
public class ProductService {
    
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts(){
        return productRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public ProductResponse createProduct(ProductRequest request){
        if (productRepository.existsBySku(request.sku())) {
            throw new DuplicateSkuException(request.sku());
        }

        Product product = new Product(request.name(), request.sku(), request.quantity(), request.price());

        Product savedProduct = productRepository.save(product);

        return toResponse(savedProduct);
    }

    public ProductResponse getProductById(Long id){

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

        return toResponse(product);
    }
    

    private ProductResponse toResponse(Product product){
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getSku(),
            product.getQuantity(),
            product.getPrice()
        );
    }

    public ProductResponse updateProduct(Long id, ProductRequest request){
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

        if (!product.getSku().equals(request.sku())
            && productRepository.existsBySku(request.sku())) {
                throw new DuplicateSkuException(request.sku());
        }


        product.setName(request.name());
        product.setSku(request.sku());
        product.setQuantity(request.quantity());
        product.setPrice(request.price());

        Product updatedProduct = productRepository.save(product);

        return toResponse(updatedProduct);
        
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(product);
    }
}
