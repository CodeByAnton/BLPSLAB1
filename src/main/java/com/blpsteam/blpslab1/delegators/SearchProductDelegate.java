package com.blpsteam.blpslab1.delegators;

import com.blpsteam.blpslab1.dto.ProductResponseDTO;
import com.blpsteam.blpslab1.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("searchProductDelegate")
@RequiredArgsConstructor
public class SearchProductDelegate implements JavaDelegate {

    private final ProductService productService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String productName = (String) delegateExecution.getVariable("productName");

        Page<ProductResponseDTO> page = productService.getApprovedProducts(productName, PageRequest.of(0, 10));
        List<ProductResponseDTO> products = page.getContent();

        if (products.isEmpty()) {
            throw new BpmnError("InvalidProductName");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(products);
            delegateExecution.setVariable("catalog", json);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при сериализации списка продуктов", e);
            delegateExecution.setVariable("catalog", "[]");
        }
    }
}
