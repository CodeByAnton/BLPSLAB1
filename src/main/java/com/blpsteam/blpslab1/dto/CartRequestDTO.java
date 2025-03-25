package com.blpsteam.blpslab1.dto;

import java.util.List;

public record CartRequestDTO(List<Long> cartItemIds, Long userId) {
}
