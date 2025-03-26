package com.blpsteam.blpslab1.dto;

import java.util.List;

public record ProductRequestDTO(String brand, String name,
                                String description, int quantity,
                                Long price, List<Long> categoryIds,
                                boolean approved) {
}
