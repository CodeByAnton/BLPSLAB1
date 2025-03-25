package com.blpsteam.blpslab1.service;

import com.blpsteam.blpslab1.dto.CategoryRequestDTO;
import com.blpsteam.blpslab1.dto.CategoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    CategoryResponseDTO getCategoryById(Long id);
    Page<CategoryResponseDTO> getAllCategories(Pageable pageable);
    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO);
    void deleteCategoryById(Long id);
}
