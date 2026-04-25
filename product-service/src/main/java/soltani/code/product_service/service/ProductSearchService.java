package soltani.code.product_service.service;


import org.springframework.stereotype.Service;
import soltani.code.product_service.entity.Product;
import soltani.code.product_service.entity.ProductDocument;
import soltani.code.product_service.repository.ProductSearchRepository;

import java.util.List;

@Service
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;

    public ProductSearchService(ProductSearchRepository productSearchRepository)
    {
        this.productSearchRepository = productSearchRepository;
    }


    public void indexProduct(Product product)
    {
        ProductDocument productDocument = new ProductDocument();

        productDocument.setName(product.getName());
        productDocument.setId(product.getId());
        productDocument.setDescription(product.getDescription());
        productDocument.setPrice(product.getPrice());
        productDocument.setStock(product.getStock());

        productSearchRepository.save(productDocument);
    }

    public List<ProductDocument> searchProducts(String keyword)
    {
        return productSearchRepository
                .findByNameContainingOrDescriptionContaining(keyword, keyword);

    }
}
