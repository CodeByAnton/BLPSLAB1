package com.blpsteam.blpslab1.delegators;

import com.blpsteam.blpslab1.exceptions.ProductNotFoundException;
import com.blpsteam.blpslab1.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("approveProductDelegate")
public class ApproveProductDelegate implements JavaDelegate {

    private final ProductService productService;

    @Override
    public void execute(DelegateExecution execution){
        Long productId = Long.valueOf((String) execution.getVariable("productId"));
        log.info("Approving product " + productId);

        try {
            productService.approveProduct(productId);
        } catch (ProductNotFoundException e) {
            throw new BpmnError("invalidProductId");
        }
    }
}