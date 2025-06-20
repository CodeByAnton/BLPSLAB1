package com.blpsteam.blpslab1.controllers;

import com.blpsteam.blpslab1.data.entities.core.Cart;
import com.blpsteam.blpslab1.dto.CartResponseDTO;
import com.blpsteam.blpslab1.service.CartService;
import com.blpsteam.blpslab1.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/buyer/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart() {
        Cart cart = cartService.getCart(userService.getUserNameFromContext());
        return ResponseEntity.ok(new CartResponseDTO(cart.getId(), cart.getUser().getUsername()));
    }

    @PreAuthorize("hasRole('BUYER')")
    @DeleteMapping
    public ResponseEntity<CartResponseDTO> clearCart() {
        Cart cart = cartService.getCart(userService.getUserNameFromContext());
        cartService.clearCart(userService.getUserNameFromContext());
        return ResponseEntity.ok(new CartResponseDTO(cart.getId(), cart.getUser().getUsername()));
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping
    public ResponseEntity<CartResponseDTO> createCart() {
        Cart cart = cartService.createCart(userService.getUserNameFromContext());
        return ResponseEntity.ok(new CartResponseDTO(cart.getId(), cart.getUser().getUsername()));
    }
}
