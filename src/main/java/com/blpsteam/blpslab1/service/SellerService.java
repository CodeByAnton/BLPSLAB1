package com.blpsteam.blpslab1.service;

import com.blpsteam.blpslab1.data.entities.Product;
import com.blpsteam.blpslab1.data.entities.User;
import com.blpsteam.blpslab1.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerService {
    private final ProductRepository productRepository;



    public SellerService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(String name, String description, Integer quantity, User seller) {
        if (name==null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (description==null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (quantity==null || quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        // Ищем существующий товар с таким же названием и описанием у данного продавца
        Optional<Product> existingProduct = productRepository.findByNameAndDescriptionAndSeller(name,description, seller);

        if (existingProduct.isPresent()) {
            // Если товар существует, суммируем количество
            Product product = existingProduct.get();
            product.setQuantity(product.getQuantity() + quantity);
            return productRepository.save(product);
        } else {
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setQuantity(quantity);
            product.setApproved(false); // По умолчанию товар не одобрен
            product.setSeller(seller);
            return productRepository.save(product);
        }


    }
}
