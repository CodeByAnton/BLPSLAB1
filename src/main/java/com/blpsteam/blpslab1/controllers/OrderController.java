package com.blpsteam.blpslab1.controllers;

import com.blpsteam.blpslab1.data.entities.Order;
import com.blpsteam.blpslab1.data.entities.User;
import com.blpsteam.blpslab1.dto.OrderResponseDTO;
import com.blpsteam.blpslab1.service.OrderService;
import com.blpsteam.blpslab1.service.UserService;
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
    private final UserService userService;
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }
    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder() {
        Order order = orderService.createOrder();
        return ResponseEntity.status(HttpStatus.CREATED).body(new OrderResponseDTO(order.getTotalPrice()));
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/pay")
    public ResponseEntity<String> payOrder() {

        orderService.payOrder();
        return new ResponseEntity<>("Payment successful", HttpStatus.OK);

    }
}
