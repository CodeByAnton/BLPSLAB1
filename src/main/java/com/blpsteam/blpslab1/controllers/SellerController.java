package com.blpsteam.blpslab1.controllers;


import com.blpsteam.blpslab1.data.entities.core.Product;

import com.blpsteam.blpslab1.dto.ProductRequestDTO;
import com.blpsteam.blpslab1.service.ProductService;
import com.blpsteam.blpslab1.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/seller")
public class SellerController {

    private final ProductService productService;
    private final UserService userService;

    public SellerController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/product")
    public ResponseEntity<?> addItem(@RequestBody ProductRequestDTO productRequestDTO) {
        Product product= productService.addProduct(
                productRequestDTO.brand(), productRequestDTO.name(),
                productRequestDTO.description(),productRequestDTO.quantity(),
                productRequestDTO.price(), 0d,
                0, userService.getUserNameFromContext()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Item %s added successfully", product.getName()));
    }
}
