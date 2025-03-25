package com.blpsteam.blpslab1.dto;

public record CartItemRequestDTO(int quantity, Long cartId,
                                 Long productId) {
}
