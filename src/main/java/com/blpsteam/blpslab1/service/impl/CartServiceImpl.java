package com.blpsteam.blpslab1.service.impl;

import com.blpsteam.blpslab1.data.entities.Cart;
import com.blpsteam.blpslab1.data.entities.CartItem;
import com.blpsteam.blpslab1.data.entities.User;
import com.blpsteam.blpslab1.data.enums.OrderStatus;
import com.blpsteam.blpslab1.exceptions.impl.CartAbsenceException;
import com.blpsteam.blpslab1.exceptions.impl.CategoryAbsenceException;
import com.blpsteam.blpslab1.exceptions.impl.UserAbsenceException;
import com.blpsteam.blpslab1.repositories.CartItemRepository;
import com.blpsteam.blpslab1.repositories.CartRepository;
import com.blpsteam.blpslab1.repositories.OrderRepository;
import com.blpsteam.blpslab1.repositories.UserRepository;
import com.blpsteam.blpslab1.service.CartItemService;
import com.blpsteam.blpslab1.service.CartService;
import com.blpsteam.blpslab1.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, UserService userService, CartItemService cartItemService, OrderRepository orderRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
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
    public Cart clearCart() {
        Long userId = userService.getUserIdFromContext();
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartAbsenceException("Корзина для пользователя с id " + userId + " не найдена"));
        if (orderRepository.findByCartIdAndStatus(cart.getId(), OrderStatus.UNPAID).isPresent()) {

            throw new RuntimeException("Cart is already used in order");
        }

        Set<CartItem> itemsToRemove = new HashSet<>(cart.getItems());

        for (CartItem item : itemsToRemove) {
            cart.removeItem(item);
            cartItemRepository.delete(item);
        }

        cart.setTotalPrice(0L);
        cartRepository.save(cart);

        return cart;

    }

    @Override
    @Transactional
    public Cart createCart() {
        Long userId = userService.getUserIdFromContext();
        if (cartRepository.findByUserId(userId).isPresent()) {
            throw new CategoryAbsenceException("У вас уже есть корзина");
        }
        Cart cart = new Cart();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserAbsenceException("Такого пользователя не существует"));
        cart.setUser(user);
        return cartRepository.save(cart);
    }
}
