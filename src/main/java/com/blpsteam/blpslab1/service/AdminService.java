package com.blpsteam.blpslab1.service;

import com.blpsteam.blpslab1.data.entities.Product;
import com.blpsteam.blpslab1.dto.ProductDTO;
import com.blpsteam.blpslab1.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    private final ProductRepository productRepository;
    public AdminService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Получить список всех товаров с названиями и описаниями (с пагинацией)
    public Page<ProductDTO> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable)
                .map(product -> new ProductDTO(product.getName(), product.getDescription(), product.getQuantity()));
    }

    // Одобрить товар по названию
    public boolean approveProduct(String name) {
        System.out.println("Approving product " + name);
        Optional<Product> productOpt = productRepository.findByName(name);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setApproved(true);
            productRepository.save(product);
            return true;
        }
        return false;
    }


}
