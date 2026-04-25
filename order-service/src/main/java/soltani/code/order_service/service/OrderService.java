package soltani.code.order_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import soltani.code.order_service.client.UserClient;
import soltani.code.order_service.dto.OrderEvent;
import soltani.code.order_service.entity.Order;
import soltani.code.order_service.exception.ResourceNotFoundException;
import soltani.code.order_service.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderEvent> orderEventKafka;
    private final UserClient userClient;



    public Order save(Order order) {

        if (!userClient.userExist(order.getUserId()))
        {
            throw new ResourceNotFoundException("User does not exist");
        }
        Order savedOrder = orderRepository.save(order);

        OrderEvent orderEvent = new OrderEvent(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getTotalPrice());

        orderEventKafka.send("order-events", orderEvent);

        return savedOrder;

    }

    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

}
