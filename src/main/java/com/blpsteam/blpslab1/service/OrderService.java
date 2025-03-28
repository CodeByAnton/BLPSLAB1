package com.blpsteam.blpslab1.service;

import com.blpsteam.blpslab1.data.entities.Order;

public interface OrderService {
    Order createOrder(Long userId);
    void payOrder(Long orderId);

}
