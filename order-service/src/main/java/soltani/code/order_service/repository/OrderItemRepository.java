package soltani.code.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soltani.code.order_service.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
