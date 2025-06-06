package com.blpsteam.blpslab1.service.impl;

import com.blpsteam.blpslab1.dto.ReviewRequestDTO;
import com.blpsteam.blpslab1.service.ReviewService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final SimpMessagingTemplate messagingTemplate;

    public ReviewServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void publishReview(ReviewRequestDTO reviewDTO) {
        // Отправляем DTO в очередь RabbitMQ через STOMP
        messagingTemplate.convertAndSend("/queue/reviews", reviewDTO);
    }
}