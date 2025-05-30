package com.blpsteam.blpslab1.service;

import com.blpsteam.blpslab1.data.entities.secondary.Order;

public interface OrderService {
    Order createOrder();
    String payOrder();
    void sendPaymentReminder(Order order);
}
