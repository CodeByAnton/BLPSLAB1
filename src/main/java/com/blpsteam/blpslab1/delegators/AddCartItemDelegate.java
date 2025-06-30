package com.blpsteam.blpslab1.delegators;

import com.blpsteam.blpslab1.data.entities.core.CartItem;
import com.blpsteam.blpslab1.dto.CartItemRequestDTO;
import com.blpsteam.blpslab1.dto.CartItemResponseDTO;
import com.blpsteam.blpslab1.service.CartItemService;
import com.blpsteam.blpslab1.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RequiredArgsConstructor
@Component("addCartItemDelegate")
public class AddCartItemDelegate implements JavaDelegate {

    private final CartItemService cartItemService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long productId = Long.valueOf((String) execution.getVariable("productId"));
        String username = (String) execution.getVariable("username");
        
        cartItemService.createCartItem(new CartItemRequestDTO(1, productId), username);
        Page<CartItemResponseDTO> cartItems = cartItemService.getAllCartItems(PageRequest.of(0, 10), username);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(cartItems);
            execution.setVariable("cart", json);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при сериализации корзины", e);
            execution.setVariable("cart", "[]");
        }
    }
}