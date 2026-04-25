package soltani.code.product_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import soltani.code.product_service.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {


}
