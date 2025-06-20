package com.blpsteam.blpslab1.delegators;

import com.blpsteam.blpslab1.exceptions.impl.UserAbsenceException;
import com.blpsteam.blpslab1.repositories.core.UserRepository;
import com.blpsteam.blpslab1.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("saveProductDelegate")
public class saveProductDelegate implements JavaDelegate {
    private final ProductService productService;
    private final UserRepository userRepository;

    @Override
    public void execute(DelegateExecution execution) {
        String username = (String) execution.getVariable("username");
        String productName = (String) execution.getVariable("name");
        String productBrand = (String) execution.getVariable("brand");
        String productDescription = (String) execution.getVariable("description");
        Integer productQuantity = Integer.valueOf((String) execution.getVariable("quantity"));
        Long productPrice = (Long) execution.getVariable("price");

        try {
            productService.addProduct(productBrand, productName, productDescription, productQuantity, productPrice, 0d, 0, username);
        } catch (IllegalArgumentException | UserAbsenceException e) {
            throw new BpmnError("invalidFields");
        }
    }
}
