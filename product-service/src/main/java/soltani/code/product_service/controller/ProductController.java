package soltani.code.product_service.controller;

import org.springframework.web.bind.annotation.*;
import soltani.code.product_service.entity.Product;
import soltani.code.product_service.entity.ProductDocument;
import soltani.code.product_service.service.ProductSearchService;
import soltani.code.product_service.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductSearchService productSearchService;

    public ProductController(ProductService productService,
                             ProductSearchService productSearchService)
    {
        this.productService = productService;
        this.productSearchService = productSearchService;
    }

    @GetMapping
    public List<Product> getProducts()
    {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id)
    {
        return productService.getProductById(id);
    }

    @PostMapping
    public Product save(@RequestBody Product product)
    {
        return productService.save(product);
    }

    @GetMapping("/search")
    public List<ProductDocument> searchProduct(@RequestParam String q)
    {
        return productSearchService.searchProducts(q);
    }


}
