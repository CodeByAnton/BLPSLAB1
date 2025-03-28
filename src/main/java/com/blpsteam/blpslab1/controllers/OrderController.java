package com.blpsteam.blpslab1.controllers;

import com.blpsteam.blpslab1.data.entities.Order;
import com.blpsteam.blpslab1.data.entities.User;
import com.blpsteam.blpslab1.dto.OrderResponseDTO;
import com.blpsteam.blpslab1.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/buyer/order")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@AuthenticationPrincipal User buyer) {
        try {
            Order order = orderService.createOrder(buyer.getId()); // Получаем userId из аутентифицированного пользователя
            return ResponseEntity.status(HttpStatus.CREATED).body(new OrderResponseDTO(buyer.getUsername(),order.getTotalPrice()));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/pay/{orderId}")
    public ResponseEntity<String> payOrder(@PathVariable Long orderId) {
        try {
            orderService.payOrder(orderId);

            return new ResponseEntity<>("Payment successful", HttpStatus.OK);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
