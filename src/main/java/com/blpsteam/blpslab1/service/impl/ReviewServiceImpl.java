package com.blpsteam.blpslab1.service.impl;

import com.blpsteam.blpslab1.dto.ReviewRequestDTO;
import com.blpsteam.blpslab1.exceptions.ReviewDataException;
import com.blpsteam.blpslab1.exceptions.impl.ReviewAbsenceException;
import com.blpsteam.blpslab1.repositories.core.ReviewRepository;
import com.blpsteam.blpslab1.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(SimpMessagingTemplate messagingTemplate, ReviewRepository reviewRepository) {
        this.messagingTemplate = messagingTemplate;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void publishReview(ReviewRequestDTO reviewDTO) {
        if (reviewRepository.findByUserIdAndProductId(reviewDTO.userId(), reviewDTO.productId()).isPresent()) {
            throw new ReviewAbsenceException("Ur review on this product already exists");
        }
        if (reviewDTO.rating() < 0 || reviewDTO.rating() > 5) {
            throw new ReviewDataException("Rating must be between 0 and 5");
        }
        messagingTemplate.convertAndSend("/queue/reviews", reviewDTO);
    }
}