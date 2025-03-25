package com.blpsteam.blpslab1.repositories;

import com.blpsteam.blpslab1.data.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
