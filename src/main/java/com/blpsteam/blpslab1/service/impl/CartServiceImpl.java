package com.blpsteam.blpslab1.service.impl;

import com.blpsteam.blpslab1.data.entities.Cart;
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
    public Cart clearCart() {
        System.out.println("Enter clearCard");
        Long userId = userService.getUserIdFromContext();
        System.out.println("Recive user from context"+userId);
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new CartAbsenceException("Корзина для пользователя с id " + userId + " не найдена"));
        if (orderRepository.findByCartIdAndStatus(cart.getId(), OrderStatus.UNPAID).isPresent()) {

            throw new RuntimeException("Cart is already used in order");
        }

        System.out.println("Clearing cart for user with ID: " + userId);
        System.out.println("Cart contains " + cart.getItems().size() + " items.");

        cartItemService.clearCartAndUpdateProductQuantities(cart.getId());
        System.out.println(cart.getItems());
        cart.getItems().clear();
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
