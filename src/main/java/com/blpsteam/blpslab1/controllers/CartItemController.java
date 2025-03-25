package com.blpsteam.blpslab1.controllers;

import com.blpsteam.blpslab1.dto.CartItemRequestDTO;
import com.blpsteam.blpslab1.dto.CartItemResponseDTO;
import com.blpsteam.blpslab1.service.CartItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping("/{id}")
    public CartItemResponseDTO getCartItemById(@PathVariable Long id) {
        return cartItemService.getCartItemById(id);
    }

    @GetMapping
    public Page<CartItemResponseDTO> getAllCartItems(Pageable pageable) {
        return cartItemService.getAllCartItems(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public CartItemResponseDTO createCartItem(@RequestBody CartItemRequestDTO cartItemRequestDTO) {
        return cartItemService.createCartItem(cartItemRequestDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public CartItemResponseDTO updateCartItem(@PathVariable Long id, @RequestBody CartItemRequestDTO cartItemRequestDTO) {
        return cartItemService.updateCartItem(id, cartItemRequestDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public void deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItemById(id);
    }
}
