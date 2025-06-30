package com.blpsteam.blpslab1.delegators;

import com.blpsteam.blpslab1.dto.ProductResponseDTO;
import com.blpsteam.blpslab1.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Slf4j
@Component("showProductsDelegate")
@RequiredArgsConstructor
public class ShowProductsDelegate implements JavaDelegate {

    private final ProductService productService;

    @Override
    public void execute(DelegateExecution execution) {
        Page<ProductResponseDTO> page = productService.getAllProducts(PageRequest.of(0, 10));
        List<ProductResponseDTO> products = page.getContent();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(products);
            execution.setVariable("productsJson", json);
        } catch (JsonProcessingException e) {
            log.error("Ошибка сериализации списка продуктов", e);
            execution.setVariable("productsJson", "[]"); // fallback на пустой JSON
        }
    }

}
