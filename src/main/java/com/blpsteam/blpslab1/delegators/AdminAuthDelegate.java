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
@Component("adminAuthDelegate")
public class AdminAuthDelegate implements JavaDelegate {
    private final UserService userService;
    @Override
    public void execute(DelegateExecution execution) {
        String username = (String) execution.getVariable("username");
        String password = (String) execution.getVariable("password");

        User user = userService.checkCredentialsAndGetUser(username, password)
                .orElseThrow(() -> new BpmnError("invalidCredentials", "Invalid username or password"));

        if (user.getRole() != Role.ADMIN) {
            throw new BpmnError("invalidCredentials", "User is not a seller");
        }
    }

}
