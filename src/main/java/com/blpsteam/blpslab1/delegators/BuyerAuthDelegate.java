package com.blpsteam.blpslab1.delegators;

import com.blpsteam.blpslab1.data.entities.core.User;
import com.blpsteam.blpslab1.data.enums.Role;
import com.blpsteam.blpslab1.repositories.core.UserRepository;
import com.blpsteam.blpslab1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("buyerAuthDelegate")
public class BuyerAuthDelegate implements JavaDelegate {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public void execute(DelegateExecution execution) {
        String username = (String) execution.getVariable("username");
        String password = (String) execution.getVariable("password");

        User user = userService.checkCredentialsAndGetUser(username, password)
                .orElseThrow(() -> new BpmnError("invalidCredentials", "Invalid username or password"));

        if (user.getRole() != Role.BUYER) {
            throw new BpmnError("invalidCredentials", "User is not a seller");
        }
//        execution.setVariable("cartAction", "ADD_PRODUCT");
    }
}