package com.blpsteam.blpslab1.repositories;

import com.blpsteam.blpslab1.data.entities.Product;
import com.blpsteam.blpslab1.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameAndDescriptionAndSeller(String name, String description, User seller);
    Optional<Product> findByName(String name);
}
