package com.blpsteam.blpslab1.controllers;

import com.blpsteam.blpslab1.data.entities.Cart;
import com.blpsteam.blpslab1.dto.CartResponseDTO;
import com.blpsteam.blpslab1.service.impl.CartServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cart")
public class CartController {

    private final CartServiceImpl cartService;

    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart() {
        Cart cart = cartService.getCart();
        return ResponseEntity.ok(new CartResponseDTO(cart.getId(), cart.getUser().getUsername()));
    }

    @DeleteMapping
    public ResponseEntity<CartResponseDTO> clearCart() {
        Cart cart = cartService.getCart();
        return ResponseEntity.ok(new CartResponseDTO(cart.getId(), cartService.clearCart()));
    }

    @PostMapping
    public ResponseEntity<CartResponseDTO> createCart() {
        Cart cart = cartService.createCart();
        return ResponseEntity.ok(new CartResponseDTO(cart.getId(), cart));
    }


}
