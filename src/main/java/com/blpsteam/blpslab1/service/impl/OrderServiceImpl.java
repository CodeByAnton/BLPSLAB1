package com.blpsteam.blpslab1.service.impl;

import com.blpsteam.blpslab1.data.entities.secondary.Cart;
import com.blpsteam.blpslab1.data.entities.secondary.Order;
import com.blpsteam.blpslab1.data.entities.secondary.User;
import com.blpsteam.blpslab1.data.enums.OrderStatus;
import com.blpsteam.blpslab1.exceptions.OrderPaymentException;
import com.blpsteam.blpslab1.exceptions.UserBalanceException;
import com.blpsteam.blpslab1.exceptions.impl.CartItemAbsenceException;
import com.blpsteam.blpslab1.exceptions.impl.OrderAbsenceException;
import com.blpsteam.blpslab1.exceptions.impl.UserAbsenceException;
import com.blpsteam.blpslab1.repositories.secondary.OrderRepository;
import com.blpsteam.blpslab1.repositories.secondary.UserRepository;
import com.blpsteam.blpslab1.service.CartService;
import com.blpsteam.blpslab1.service.OrderService;
import com.blpsteam.blpslab1.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, CartService cartService, UserService userService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.userService = userService;
    }
    @Override
    @Transactional(transactionManager = "jtaTransactionManager")
    public Order createOrder() {
        log.info("CreateOrder method");
        Long userId=userService.getUserIdFromContext();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserAbsenceException("User not found"));

        if (orderRepository.existsByUserIdAndStatus(user.getId(), OrderStatus.UNPAID)) {
            throw new OrderPaymentException("User already has an unpaid order");
        }

        Cart cart = cartService.getCart();

        if (cart.getItems().isEmpty()) {
            throw new CartItemAbsenceException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.UNPAID); // Изначально статус "не оплачено"
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);
        log.info("Order created by user {}", user.getId());
        schedulePaymentReminder(order);
        return order;
    }

    @Override
    @Transactional(transactionManager = "jtaTransactionManager")
    public void payOrder() {
        log.info("PayOrder method");
        Long userId=userService.getUserIdFromContext();
        User buyer=userRepository.findById(userId).orElseThrow(() -> new UserAbsenceException("User not found"));
        Order order = orderRepository.findByUserAndStatus(buyer, OrderStatus.UNPAID)
                .orElseThrow(() -> new OrderAbsenceException("Order not found"));

        if (order.getStatus() != OrderStatus.UNPAID) {
            throw new OrderPaymentException("Order already processed or paid");
        }

        User user = order.getUser();
        System.out.println(user.getBalance());
        System.out.println(order.getTotalPrice());
        if (user.getBalance() < order.getTotalPrice()) {
            // Выводим сообщение в консоль вместо отправки в поддержку
            System.out.println("Payment failed: User " + user.getUsername() + " has insufficient funds for order " + order.getId());
            throw new UserBalanceException("Insufficient balance");
        }

        // Снимаем деньги с баланса пользователя
        user.setBalance(user.getBalance() - order.getTotalPrice());
        order.setStatus(OrderStatus.PAID); // Обновляем статус на "оплачено"

        userRepository.save(user);
        orderRepository.save(order);

        // Очищаем корзину после успешной оплаты
        cartService.clearCartAfterPayment();

        // Выводим сообщение о успешной оплате
        log.info("Payment successful: Order {} has been payed. Cart cleared", order.getId());
        System.out.println("Payment successful: Order " + order.getId() + " paid. Cart cleared.");
    }



    private void schedulePaymentReminder(Order order) {
        Executors.newSingleThreadScheduledExecutor().schedule(
                () -> {
                    Order order1 = orderRepository.findById(order.getId())
                            .orElseThrow(() -> new OrderAbsenceException("Order not found"));

                    // Отправлять напоминание только если заказ все еще неоплачен
                    if ( order1.getStatus()== OrderStatus.UNPAID) {
                        sendPaymentReminder(order);
                    }
                },
                1, TimeUnit.MINUTES);

    }

    private void sendPaymentReminder(Order order) {
        User user = order.getUser();
        String message = "User with name " + user.getUsername() + ", your order #" + order.getId() + " is still unpaid. Please complete the payment.";

        System.out.println("Payment reminder: " + message);
    }
}
