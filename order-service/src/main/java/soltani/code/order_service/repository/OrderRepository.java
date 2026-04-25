package soltani.code.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soltani.code.order_service.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userid);
}
