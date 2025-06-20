package com.blpsteam.blpslab1.service;

import com.blpsteam.blpslab1.data.entities.core.Cart;

public interface CartService {
    Cart getCart(String username);
    void clearCart(String username);
    Cart createCart(String username);
//    void clearCartAfterPayment();
    void clearCartAfterPayment(String username);
}
