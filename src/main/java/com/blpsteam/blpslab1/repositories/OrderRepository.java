package com.blpsteam.blpslab1.repositories;

import com.blpsteam.blpslab1.data.entities.Order;
import com.blpsteam.blpslab1.data.entities.User;
import com.blpsteam.blpslab1.data.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
    boolean existsByUserAndStatus(User user, OrderStatus status);
    Optional<Order> findByUserAndStatus(User user,OrderStatus status);
//    Optional<Order> findByCartIdAndStatus(Long cartId, OrderStatus status);


}
