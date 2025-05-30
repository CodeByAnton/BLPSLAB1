package com.blpsteam.blpslab1.repositories.secondary;

import com.blpsteam.blpslab1.data.entities.secondary.Order;
import com.blpsteam.blpslab1.data.entities.secondary.User;
import com.blpsteam.blpslab1.data.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
    boolean existsByUserIdAndStatus(Long userId, OrderStatus status);
    Optional<Order> findByUserAndStatus(User buyer,OrderStatus status);
    List<Order> findByStatusAndCreatedAtBeforeAndReminderSentFalse(OrderStatus status, LocalDateTime time);

//    Optional<Order> findByCartIdAndStatus(Long cartId, OrderStatus status);
}
