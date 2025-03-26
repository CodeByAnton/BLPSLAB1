package com.blpsteam.blpslab1.service.impl;

import com.blpsteam.blpslab1.data.entities.Cart;
import com.blpsteam.blpslab1.data.entities.CartItem;
import com.blpsteam.blpslab1.data.entities.Product;
import com.blpsteam.blpslab1.dto.CartItemRequestDTO;
import com.blpsteam.blpslab1.dto.CartItemResponseDTO;
import com.blpsteam.blpslab1.exceptions.CartItemQuantityException;
import com.blpsteam.blpslab1.exceptions.impl.CartAbsenceException;
import com.blpsteam.blpslab1.exceptions.impl.ProductAbsenceException;
import com.blpsteam.blpslab1.repositories.CartItemRepository;
import com.blpsteam.blpslab1.repositories.CartRepository;
import com.blpsteam.blpslab1.repositories.ProductRepository;
import com.blpsteam.blpslab1.service.CartItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, ProductRepository productRepository, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public CartItemResponseDTO getCartItemById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem с данным id не существует"));
        return getCartItemResponseDTOFromEntity(cartItem);
    }

    @Override
    public Page<CartItemResponseDTO> getAllCartItems(Pageable pageable) {
        return cartItemRepository.findAll(pageable)
                .map(this::getCartItemResponseDTOFromEntity);
    }

    @Override
    @Transactional
    public CartItemResponseDTO createCartItem(CartItemRequestDTO cartItemRequestDTO) {
        CartItem cartItem = getCartItemFromDTO(cartItemRequestDTO);
        Product product = cartItem.getProduct();
        int newQuantity = product.getQuantity() - cartItem.getQuantity();
        if (newQuantity >= 0) {
            product.setQuantity(newQuantity);
            productRepository.save(product);
            cartItem = cartItemRepository.save(cartItem);
            return getCartItemResponseDTOFromEntity(cartItem);
        }
        throw new CartItemQuantityException("Недостаточно товара");
    }

    @Override
    @Transactional
    public CartItemResponseDTO updateCartItem(Long id, CartItemRequestDTO cartItemRequestDTO) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem с данным id не существует"));

        int unitPrice = cartItem.getProduct().getPrice().intValue();
        int totalPrice = unitPrice * cartItemRequestDTO.quantity();

        cartItem.setQuantity(cartItemRequestDTO.quantity());
        Product product = cartItem.getProduct();
        int newQuantity = product.getQuantity() - cartItem.getQuantity();
        if (newQuantity >= 0) {
            product.setQuantity(newQuantity);
            cartItem.setUnitPrice(unitPrice);
            cartItem.setTotalPrice(totalPrice);

            cartItemRepository.save(cartItem);

            return getCartItemResponseDTOFromEntity(cartItem);
        }
        throw new CartItemQuantityException("Недостаточно товара");
    }

    @Override
    @Transactional
    public void deleteCartItemById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem с данным id не существует"));
        Product product = cartItem.getProduct();
        int quantity = product.getQuantity();
        product.setQuantity(quantity+cartItem.getQuantity());
        productRepository.save(product);
        cartItemRepository.delete(cartItem);
    }


    private CartItem getCartItemFromDTO(CartItemRequestDTO cartItemRequestDTO) {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(cartItemRequestDTO.quantity());
        Product product = productRepository.findById(cartItemRequestDTO.productId())
                .orElseThrow(() -> new ProductAbsenceException("Product с данным id не существует"));
        Cart cart = cartRepository.findById(cartItemRequestDTO.cartId())
                .orElseThrow(()-> new CartAbsenceException("Cart с данным id не существует"));
        cartItem.setProduct(product);
        cartItem.setUnitPrice(product.getPrice().intValue());
        cartItem.setTotalPrice(product.getPrice().intValue()*cartItemRequestDTO.quantity());
        cartItem.setCart(cart);
        return cartItem;
    }


    private CartItemResponseDTO getCartItemResponseDTOFromEntity(CartItem cartItem) {
        return new CartItemResponseDTO(
                cartItem.getId(),
                cartItem.getQuantity(),
                cartItem.getUnitPrice(),
                cartItem.getTotalPrice(),
                cartItem.getCart().getId(),
                cartItem.getProduct().getId()
        );
    }


}
