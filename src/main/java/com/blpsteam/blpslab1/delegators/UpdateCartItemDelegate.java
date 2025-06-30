package com.blpsteam.blpslab1.delegators;

import com.blpsteam.blpslab1.dto.CartItemQuantityRequestDTO;
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

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RequiredArgsConstructor
@Component("updateCartItemDelegate")
public class UpdateCartItemDelegate implements JavaDelegate {

    private final CartItemService cartItemService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String username = (String) execution.getVariable("username");
        String stringQuantity = (String) execution.getVariable("newQuantity");
        if (!stringQuantity.isEmpty() && !username.isEmpty()) {
            int newQuantity = Integer.parseInt(stringQuantity);
            Long cartItemId = Long.valueOf((String) execution.getVariable("cartItemId"));
            cartItemService.updateCartItem(cartItemId, new CartItemQuantityRequestDTO(newQuantity), username);

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
}