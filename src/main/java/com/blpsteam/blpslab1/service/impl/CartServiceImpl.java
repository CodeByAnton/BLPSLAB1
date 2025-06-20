package com.blpsteam.blpslab1.service.impl;

import com.blpsteam.blpslab1.data.entities.core.Cart;
import com.blpsteam.blpslab1.data.entities.core.User;
import com.blpsteam.blpslab1.data.enums.OrderStatus;
import com.blpsteam.blpslab1.exceptions.impl.CartAbsenceException;
import com.blpsteam.blpslab1.exceptions.impl.UserAbsenceException;
import com.blpsteam.blpslab1.repositories.core.CartRepository;
import com.blpsteam.blpslab1.repositories.core.OrderRepository;
import com.blpsteam.blpslab1.repositories.core.UserRepository;
import com.blpsteam.blpslab1.service.CartItemService;
import com.blpsteam.blpslab1.service.CartService;
import com.blpsteam.blpslab1.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);
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
    public Cart getCart(String username) {
        Long userId = userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UserAbsenceException("Такого пользователя не существует"));
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartAbsenceException("Корзина для пользователя с id " + userId + " не найдена"));
        Long total = cart.getTotalPrice();
        cart.setTotalPrice(total);
        return cart;
    }

    @Override
    @Transactional
    public void clearCart(String username) {
        log.info("ClearCart method");
        Long userId = userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UserAbsenceException("Такого пользователя не существует"));

        if (orderRepository.existsByUserIdAndStatus(userId, OrderStatus.UNPAID)){
            throw new IllegalArgumentException("You can't clear cart while you have unpaid order");
        };

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartAbsenceException("Корзина для пользователя с id " + userId + " не найдена"));

        cartItemService.clearCartAndUpdateProductQuantities(cart.getId());
        cart.getItems().clear();
        cart.setTotalPrice(0L);
        cartRepository.save(cart);
        log.info("Cart has been cleared for user {}", userId);
    }

    @Override
    @Transactional
    public Cart createCart(String username) {
        log.info("CreateCart method");
        Long userId = userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UserAbsenceException("Такого пользователя не существует"));
        if (cartRepository.findByUserId(userId).isPresent()) {
            throw new CartAbsenceException("You already have a cart");
        }
        Cart cart = new Cart();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserAbsenceException("User with this id not found"));
        cart.setUser(user);
        log.info("Cart has been created for user {}", userId);
        return cartRepository.save(cart);
    }

//    @Override
//    @Transactional
//    public void clearCartAfterPayment() {
//        log.info("ClearCartAfterPayment method");
//        Long userId = userService.getUserIdFromContext();
//
//        if (orderRepository.existsByUserIdAndStatus(userId, OrderStatus.UNPAID)){
//            throw new IllegalArgumentException("You can't clear cart while you have unpaid order");
//        };
//
//        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartAbsenceException("Cart for user with id= " + userId + " not found"));
//
//        System.out.println(cart.getItems());
//        cart.getItems().clear();
//        cart.setTotalPrice(0L);
//        cartRepository.save(cart);
//        log.info("Cart has been cleared after payment for user {}", userId);
//    }

    @Override
    @Transactional
    public void clearCartAfterPayment(String username) {
        Long userId = userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UserAbsenceException("Такого пользователя не существует"));
        log.info("ClearCartAfterPayment method");
        if (orderRepository.existsByUserIdAndStatus(userId, OrderStatus.UNPAID)){
            throw new IllegalArgumentException("You can't clear cart while you have unpaid order");
        };

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartAbsenceException("Cart for user with id= " + userId + " not found"));

        System.out.println(cart.getItems());
        cart.getItems().clear();
        cart.setTotalPrice(0L);
        cartRepository.save(cart);
        log.info("Cart has been cleared after payment for user {}", userId);
    }
}
