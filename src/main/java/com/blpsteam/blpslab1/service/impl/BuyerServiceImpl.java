package com.blpsteam.blpslab1.service.impl;

import com.blpsteam.blpslab1.data.entities.secondary.User;
import com.blpsteam.blpslab1.exceptions.UsernameNotFoundException;
import com.blpsteam.blpslab1.repositories.secondary.UserRepository;
import com.blpsteam.blpslab1.service.BuyerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BuyerServiceImpl implements BuyerService {
    private static final Logger log = LoggerFactory.getLogger(BuyerServiceImpl.class);
    private final UserRepository userRepository;
    public BuyerServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void increaseBalance(String username, Long amount) {
        log.debug("Increasing user balance method");
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setBalance(user.getBalance()+amount);
        log.info("Increase for user {}, new balance = {}", user.getId(), user.getBalance());
        userRepository.save(user);
    }
}
