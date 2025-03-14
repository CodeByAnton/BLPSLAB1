package com.blpsteam.blpslab1.controllers;

import com.blpsteam.blpslab1.dto.ProductDTO;
import com.blpsteam.blpslab1.dto.ProductNameDTO;
import com.blpsteam.blpslab1.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/showallproducts")
    public Page<ProductDTO> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return adminService.getAllProducts(page, size);
    }

    // Одобрение товара по названию
    @PutMapping("/approve")
    public ResponseEntity<String> approveProduct(@RequestBody ProductNameDTO productNameDTO) {
        System.out.println(productNameDTO);
        boolean updated = adminService.approveProduct(productNameDTO.getName());
        if (updated) {
            return ResponseEntity.ok("Product approved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

}
