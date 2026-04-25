package soltani.code.product_service.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import soltani.code.product_service.entity.Product;
import soltani.code.product_service.exception.ResourceNotFoundException;
import soltani.code.product_service.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductSearchService productSearchService;

    public ProductService(ProductRepository productRepository , ProductSearchService productSearchService) {
        this.productRepository = productRepository;
        this.productSearchService = productSearchService;
    }

    @Cacheable("products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public Product save(Product product) {

        Product savedProduct = productRepository.save(product);
        productSearchService.indexProduct(savedProduct);
        return savedProduct;
    }

    @Cacheable(value = "product" , key = "#productId")
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow
                (() -> new ResourceNotFoundException
                        ("Product not found with id: " + productId));

    }
}