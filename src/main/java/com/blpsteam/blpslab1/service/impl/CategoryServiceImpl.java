package com.blpsteam.blpslab1.service.impl;

import com.blpsteam.blpslab1.data.entities.Category;
import com.blpsteam.blpslab1.dto.CategoryRequestDTO;
import com.blpsteam.blpslab1.dto.CategoryResponseDTO;
import com.blpsteam.blpslab1.exceptions.impl.CategoryAbsenceException;
import com.blpsteam.blpslab1.repositories.CategoryRepository;
import com.blpsteam.blpslab1.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryAbsenceException("Category с данным id не существует"));
        return getCategoryResponseDTOFromEntity(category);
    }

    @Override
    public Page<CategoryResponseDTO> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(this::getCategoryResponseDTOFromEntity);
    }

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryRepository.save(getCategoryFromDTO(categoryRequestDTO));
        return getCategoryResponseDTOFromEntity(category);
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category sourceCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryAbsenceException("Category с данным id не существует"));
        sourceCategory.setName(categoryRequestDTO.name());
        Category updatedCategory = categoryRepository.save(sourceCategory);
        return getCategoryResponseDTOFromEntity(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new CategoryAbsenceException("Category с данным id не существует");
                });
    }

    private Category getCategoryFromDTO(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category();
        category.setName(categoryRequestDTO.name());
        return category;
    }

    private CategoryResponseDTO getCategoryResponseDTOFromEntity(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName());
    }
}
