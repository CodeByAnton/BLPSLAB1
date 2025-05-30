package com.blpsteam.blpslab1.util;

import com.blpsteam.blpslab1.data.entities.secondary.Order;
import com.blpsteam.blpslab1.data.enums.OrderStatus;
import com.blpsteam.blpslab1.repositories.secondary.OrderRepository;
import com.blpsteam.blpslab1.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderReminderScheduler {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Scheduled(fixedRate = 60_000) // каждые 60 секунд
    @Transactional
    public void checkUnpaidOrdersAndSendReminders() {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);

        List<Order> ordersToRemind = orderRepository
                .findByStatusAndCreatedAtBeforeAndReminderSentFalse(OrderStatus.UNPAID, oneMinuteAgo);

        for (Order order : ordersToRemind) {
            try {
                orderService.sendPaymentReminder(order);
                order.setReminderSent(true);
            } catch (Exception e) {
                log.warn("Failed to send reminder for order {}: {}", order.getId(), e.getMessage());
            }
        }
    }
}