package com.blpsteam.blpslab1.delegators;

import com.blpsteam.blpslab1.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component("createOrderDelegate")
public class CreateOrderDelegate implements JavaDelegate {

    private final OrderService orderService;

    @Override
    public void execute(DelegateExecution execution){
        String username = (String) execution.getVariable("username");
        String link=orderService.createOrder(username);
        execution.setVariable("link", link);
    }
}
