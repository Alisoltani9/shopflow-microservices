package soltani.code.order_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import soltani.code.order_service.dto.OrderEvent;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @KafkaListener(topics = "order-events",groupId = "shopflow-group")
    public void handleOrderEvent(OrderEvent orderEvent)
    {
        log.info("Email sent to user {} for order {}", orderEvent.getUserId(), orderEvent.getOrderId());
    }
}
