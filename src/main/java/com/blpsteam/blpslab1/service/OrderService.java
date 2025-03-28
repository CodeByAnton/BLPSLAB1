package com.blpsteam.blpslab1.service;

import com.blpsteam.blpslab1.data.entities.Order;
import com.blpsteam.blpslab1.data.entities.User;

public interface OrderService {
    Order createOrder(Long userId);
    void payOrder(User buyer);

}
