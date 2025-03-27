package com.blpsteam.blpslab1.controllers;

import com.blpsteam.blpslab1.dto.ProductResponseDTO;
import com.blpsteam.blpslab1.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/buyer")
public class BuyerController {
    private final ProductService productService;
    public BuyerController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/catalog")
    public Page<ProductResponseDTO> getCatalog(@RequestParam(required = false) String name,
                                               Pageable pageable) {
        return productService.getApprovedProducts(name,pageable);
    }
}
