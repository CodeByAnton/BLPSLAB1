package com.blpsteam.blpslab1.service.impl;

import com.blpsteam.blpslab1.data.entities.Cart;
import com.blpsteam.blpslab1.data.entities.User;
import com.blpsteam.blpslab1.data.enums.OrderStatus;
import com.blpsteam.blpslab1.exceptions.impl.CartAbsenceException;
import com.blpsteam.blpslab1.exceptions.impl.UserAbsenceException;
import com.blpsteam.blpslab1.repositories.CartRepository;
import com.blpsteam.blpslab1.repositories.OrderRepository;
import com.blpsteam.blpslab1.repositories.UserRepository;
import com.blpsteam.blpslab1.service.CartItemService;
import com.blpsteam.blpslab1.service.CartService;
import com.blpsteam.blpslab1.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CartItemService cartItemService;
    private final OrderRepository orderRepository;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, UserService userService, CartItemService cartItemService, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.cartItemService = cartItemService;

        this.orderRepository = orderRepository;
    }

    @Override
    public Cart getCart() {
        Long userId = userService.getUserIdFromContext();
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartAbsenceException("Корзина для пользователя с id " + userId + " не найдена"));
        Long total = cart.getTotalPrice();
        cart.setTotalPrice(total);
        return cart;
    }

    @Override
    @Transactional
    public void clearCart() {
        Long userId = userService.getUserIdFromContext();

        if (orderRepository.existsByUserIdAndStatus(userId, OrderStatus.UNPAID)){
            throw new IllegalArgumentException("You can't clear cart while you have unpaid order");
        };

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartAbsenceException("Корзина для пользователя с id " + userId + " не найдена"));


        cartItemService.clearCartAndUpdateProductQuantities(cart.getId());
        System.out.println(cart.getItems());
        cart.getItems().clear();
        cartRepository.delete(cart);

    }

    @Override
    @Transactional
    public Cart createCart() {
        Long userId = userService.getUserIdFromContext();
        if (cartRepository.findByUserId(userId).isPresent()) {
            throw new CartAbsenceException("You already have a cart");
        }
        Cart cart = new Cart();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserAbsenceException("User with this id not found"));
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void clearCartAfterPayment() {
        Long userId = userService.getUserIdFromContext();

        if (orderRepository.existsByUserIdAndStatus(userId, OrderStatus.UNPAID)){
            throw new IllegalArgumentException("You can't clear cart while you have unpaid order");
        };

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartAbsenceException("Cart for user with id= " + userId + " not found"));


        System.out.println(cart.getItems());
        cartItemService.clearCartAndUpdateProductQuantities(cart.getId());
        cart.getItems().clear();
        cart.setTotalPrice(0L);
        cartRepository.save(cart);
    }
}
