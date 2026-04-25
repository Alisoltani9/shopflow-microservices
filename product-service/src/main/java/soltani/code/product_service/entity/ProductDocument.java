package soltani.code.product_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;

@Document(indexName = "products")
@Data
public class ProductDocument {

    @Id
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
}
