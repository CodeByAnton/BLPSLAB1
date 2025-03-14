package com.blpsteam.blpslab1.controllers;


import com.blpsteam.blpslab1.data.entities.Product;
import com.blpsteam.blpslab1.data.entities.User;
import com.blpsteam.blpslab1.dto.ProductDTO;
import com.blpsteam.blpslab1.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/seller")
public class SellerController {



    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }
    /**
     * Принимает имя товара, описание и количество, если дважды подать товар с одинм и тем же описанием и названием
     * , то его количество увеличится, а нового товара не будет создано, если же такого описания и/или имени нет,
     * то создается новая запись о товаре в таблице. Все товары по умолчанию имею approved=false и не будут видны
     * покупателям, пока администратор не сменит их статус на approved=true
     */

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/addproduct")
    public ResponseEntity<?> addItem(@RequestBody ProductDTO productDTO,
                                     @AuthenticationPrincipal User seller) {

            Product product= sellerService.addProduct(productDTO.getName(),productDTO.getDescription(),productDTO.getQuantity(), seller);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Item %s added successfully", product.getName()));



    }
}
