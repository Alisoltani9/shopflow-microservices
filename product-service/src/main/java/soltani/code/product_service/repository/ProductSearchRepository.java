package soltani.code.product_service.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import soltani.code.product_service.entity.ProductDocument;

import java.util.List;

public interface ProductSearchRepository extends
        ElasticsearchRepository<ProductDocument, Long> {

    List<ProductDocument> findByNameContainingOrDescriptionContaining
            (String name, String description);

}
