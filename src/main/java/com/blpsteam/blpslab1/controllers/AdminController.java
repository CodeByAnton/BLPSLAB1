package com.blpsteam.blpslab1.controllers;

import com.blpsteam.blpslab1.dto.ProductRequestNameDTO;
import com.blpsteam.blpslab1.dto.ProductResponseDTO;
import com.blpsteam.blpslab1.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final ProductService productService;
    public AdminController(ProductService productService) {

        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/showallproducts")
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    // Одобрение товара по названию
    @PutMapping("/approve")
    public ResponseEntity<String> approveProduct(@RequestBody ProductRequestNameDTO productNameDTO) {
        System.out.println(productNameDTO);
        boolean updated = productService.approveProduct(productNameDTO.name());
        if (updated) {
            return ResponseEntity.ok("Product approved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

}
