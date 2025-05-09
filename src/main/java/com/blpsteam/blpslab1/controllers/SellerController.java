package com.blpsteam.blpslab1.controllers;


import com.blpsteam.blpslab1.data.entities.primary.Product;

import com.blpsteam.blpslab1.dto.ProductRequestDTO;
import com.blpsteam.blpslab1.service.ProductService;
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

    public SellerController(ProductService productService) {
        this.productService = productService;
    }
    /**
     * Принимает имя товара, описание и количество, если дважды подать товар с одинм и тем же описанием и названием
     * , то его количество увеличится, а нового товара не будет создано, если же такого описания и/или имени нет,
     * то создается новая запись о товаре в таблице. Все товары по умолчанию имею approved=false и не будут видны
     * покупателям, пока администратор не сменит их статус на approved=true
     */

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/product")
    public ResponseEntity<?> addItem(@RequestBody ProductRequestDTO productRequestDTO) {
        Product product= productService.addProduct(productRequestDTO.brand(), productRequestDTO.name(), productRequestDTO.description(),productRequestDTO.quantity(),productRequestDTO.price());
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Item %s added successfully", product.getName()));
    }
}
