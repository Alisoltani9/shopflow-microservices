package soltani.code.order_service.controller;

import org.springframework.web.bind.annotation.*;
import soltani.code.order_service.entity.Order;
import soltani.code.order_service.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/my")
    public List<Order> getUserOrders(@RequestParam Long userId) {
        return orderService.findByUserId(userId);
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.save(order);
    }
}