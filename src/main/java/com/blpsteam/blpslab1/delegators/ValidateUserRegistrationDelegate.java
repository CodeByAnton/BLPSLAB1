package com.blpsteam.blpslab1.delegators;


import com.blpsteam.blpslab1.data.enums.Role;
import com.blpsteam.blpslab1.exceptions.AdminAlreadyExistsException;
import com.blpsteam.blpslab1.exceptions.UsernameAlreadyExistsException;
import com.blpsteam.blpslab1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Component("validateUserRegistrationDelegate")
@RequiredArgsConstructor
@Slf4j
public class ValidateUserRegistrationDelegate implements JavaDelegate {
    private final UserService userService;

    @Override
    public void execute(DelegateExecution execution){
        log.info("validateUserRegistrationDelegate");
        String username = (String) execution.getVariable("username");
        String password = (String) execution.getVariable("password");
        String roleStr = (String) execution.getVariable("role");
        try {
            Role role = Role.valueOf(roleStr);
            userService.register(username, password, role);
        } catch (IllegalArgumentException | AdminAlreadyExistsException | UsernameAlreadyExistsException e) {
            log.error(e.getMessage());
            throw new BpmnError("invalidArgumentsError");
        }
    }
}
