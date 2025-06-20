package com.blpsteam.blpslab1.controllers;


import com.blpsteam.blpslab1.dto.OrderResponseDTO;
import com.blpsteam.blpslab1.service.OrderService;
import com.blpsteam.blpslab1.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder() {
        String paymentLink = orderService.createOrder(userService.getUserNameFromContext());
        return ResponseEntity.status(HttpStatus.CREATED).body(new OrderResponseDTO(paymentLink));
    }
}
