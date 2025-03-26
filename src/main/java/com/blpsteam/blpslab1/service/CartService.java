package com.blpsteam.blpslab1.service;

import com.blpsteam.blpslab1.data.entities.Cart;
import com.blpsteam.blpslab1.dto.CartRequestDTO;

import java.util.Optional;

public interface CartService {
    Cart getCart(Long userId);
    Cart clearCart(Long userId);
    Cart createCart(Long userId);
}
