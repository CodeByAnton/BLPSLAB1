package com.blpsteam.blpslab1.service.impl;

import com.blpsteam.blpslab1.data.entities.Category;
import com.blpsteam.blpslab1.data.entities.Product;
import com.blpsteam.blpslab1.dto.ProductRequestDTO;
import com.blpsteam.blpslab1.dto.ProductResponseDTO;
import com.blpsteam.blpslab1.exceptions.impl.CategoryAbsenceException;
import com.blpsteam.blpslab1.exceptions.impl.ProductAbsenceException;
import com.blpsteam.blpslab1.exceptions.impl.UserAbsenceException;
import com.blpsteam.blpslab1.repositories.CategoryRepository;
import com.blpsteam.blpslab1.repositories.ProductRepository;
import com.blpsteam.blpslab1.repositories.UserRepository;
import com.blpsteam.blpslab1.service.ProductService;
import com.blpsteam.blpslab1.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository, UserService userService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductAbsenceException("Product с данным id не существует"));
        return getProductResponseDTOFromEntity(product);
    }

    @Override
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::getProductResponseDTOFromEntity);
    }

    @Override
    public Page<ProductResponseDTO> getApprovedProducts(Pageable pageable) {
        return productRepository.findByApproved(true, pageable).map(this::getProductResponseDTOFromEntity);
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = getProductFromDTO(productRequestDTO);
        Product newProduct = productRepository.save(product);
        return getProductResponseDTOFromEntity(newProduct);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductAbsenceException("Product с данным id не существует"));

        existingProduct.setBrand(productRequestDTO.brand());
        existingProduct.setName(productRequestDTO.name());
        existingProduct.setDescription(productRequestDTO.description());
        existingProduct.setQuantity(productRequestDTO.quantity());
        existingProduct.setPrice(productRequestDTO.price());
        existingProduct.setApproved(productRequestDTO.approved());
        if (!productRequestDTO.categoryIds().isEmpty()) {
            List<Category> categories = new ArrayList<>();

            for (Long categoryId : productRequestDTO.categoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryAbsenceException("Категория с ID " + categoryId + " не найдена"));
                categories.add(category);
            }

            existingProduct.setCategories(categories);
        }
        Product updatedProduct = productRepository.save(existingProduct);
        return getProductResponseDTOFromEntity(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete, () -> {
                    throw new ProductAbsenceException("Product с данным id не существует");
                });
    }

    private Product getProductFromDTO(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setBrand(productRequestDTO.brand());
        product.setName(productRequestDTO.name());
        product.setDescription(productRequestDTO.description());
        product.setQuantity(productRequestDTO.quantity());
        product.setPrice(productRequestDTO.price());
        product.setApproved(productRequestDTO.approved());
        product.setSeller(userRepository.findById(userService.getUserIdFromContext())
                .orElseThrow(() -> new UserAbsenceException("Нет такого пользователя"))
        );

        if (!productRequestDTO.categoryIds().isEmpty()) {
            List<Category> categories = new ArrayList<>();

            for (Long categoryId : productRequestDTO.categoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryAbsenceException("Категория с ID " + categoryId + " не найдена"));
                categories.add(category);
            }

            product.setCategories(categories);
        }

        return product;
    }

    private ProductResponseDTO getProductResponseDTOFromEntity(Product product) {
        List<Long> categoryIds = product.getCategories() != null && !product.getCategories().isEmpty() ?
                product.getCategories().stream().map(Category::getId).collect(Collectors.toList()) :
                Collections.emptyList();

        return new ProductResponseDTO(
                product.getId(),
                product.getBrand(),
                product.getName(),
                product.getDescription(),
                product.getQuantity(),
                product.getPrice(),
                categoryIds
        );
    }
}
