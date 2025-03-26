package com.blpsteam.blpslab1.controllers;

import com.blpsteam.blpslab1.data.entities.Cart;
import com.blpsteam.blpslab1.dto.CartRequestDTO;
import com.blpsteam.blpslab1.dto.CartResponseDTO;
import com.blpsteam.blpslab1.service.CartServiceImpl;
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
    public ResponseEntity<CartResponseDTO> getCart(@RequestBody CartRequestDTO cartRequestDTO) {
        Cart cart = cartService.getCart(cartRequestDTO.userId());
        return ResponseEntity.ok(new CartResponseDTO(cart.getId(), cart.getUser().getUsername()));
    }

    @DeleteMapping
    public ResponseEntity<CartResponseDTO> clearCart(@RequestBody CartRequestDTO cartRequestDTO) {
        Cart cart = cartService.getCart(cartRequestDTO.userId());
        return ResponseEntity.ok(new CartResponseDTO(cart.getId(), cartService.clearCart(cartRequestDTO.userId())));
    }

    @PostMapping
    public ResponseEntity<CartResponseDTO> createCart(@RequestBody CartRequestDTO cartRequestDTO) {
        Cart cart = cartService.createCart(cartRequestDTO.userId());
        return ResponseEntity.ok(new CartResponseDTO(cart.getId(), cart));
    }


}
