package com.blpsteam.blpslab1.service.impl;

import com.blpsteam.blpslab1.data.entities.Product;
import com.blpsteam.blpslab1.data.entities.User;
import com.blpsteam.blpslab1.dto.ProductResponseDTO;
import com.blpsteam.blpslab1.exceptions.ProductNotFoundException;
import com.blpsteam.blpslab1.exceptions.impl.ProductAbsenceException;
import com.blpsteam.blpslab1.repositories.ProductRepository;
import com.blpsteam.blpslab1.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
    public Page<ProductResponseDTO> getApprovedProducts(String name, Pageable pageable) {
        Page<Product> products;

        if (name != null && !name.isEmpty()) {
            products = productRepository.findByApprovedAndNameContainingIgnoreCase(true,name, pageable);
            System.out.println(products);
            if (products.isEmpty()) {
                throw new ProductNotFoundException("No product with that name was found. Please change the name you are entering.");
            }
        } else {
            products = productRepository.findByApproved(true, pageable);
        }

        return products.map(this::getProductResponseDTOFromEntity);
    }

    @Override
    public Product addProduct(String brand, String name, String description, int quantity, Long price, User seller) {
        if (name==null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (description==null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        // Ищем существующий товар с таким же названием и описанием у данного продавца
        Optional<Product> existingProduct = productRepository.findByBrandAndNameAndDescriptionAndSeller(brand,name,description, seller);

        if (existingProduct.isPresent()) {
            // Если товар существует, суммируем количество
            Product product = existingProduct.get();
            product.setQuantity(product.getQuantity() + quantity);
            return productRepository.save(product);
        } else {
            Product product = new Product();
            product.setBrand(brand);
            product.setName(name);
            product.setDescription(description);
            product.setQuantity(quantity);
            product.setPrice(price);
            product.setApproved(false); // По умолчанию товар не одобрен
            product.setSeller(seller);
            return productRepository.save(product);
        }
    }

    @Override
    public boolean approveProduct(Long productId) {
        System.out.println("Approving product id" + productId);
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setApproved(true);
            productRepository.save(product);
            return true;
        }
        throw new ProductNotFoundException("No product with that id was found. Please change the id you are entering.");

    }


    private ProductResponseDTO getProductResponseDTOFromEntity(Product product) {

        return new ProductResponseDTO(
                product.getId(),
                product.getBrand(),
                product.getName(),
                product.getDescription(),
                product.getQuantity(),
                product.getPrice(),
                product.getApproved()

        );
    }


}
