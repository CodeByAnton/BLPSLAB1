package com.blpsteam.blpslab1.dto;

import java.util.List;

public record ProductResponseDTO(Long id, String brand, String name,
                                 String description, int quantity,
                                 Long price, List<Long> categoryIds) {
}
