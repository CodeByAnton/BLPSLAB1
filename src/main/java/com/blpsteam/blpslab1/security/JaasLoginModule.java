package com.blpsteam.blpslab1.security;

import com.blpsteam.blpslab1.data.entities.secondary.User;
import com.blpsteam.blpslab1.exceptions.InvalidCredentialsException;
import com.blpsteam.blpslab1.exceptions.UsernameNotFoundException;
import com.blpsteam.blpslab1.repositories.secondary.UserRepository;
import com.blpsteam.blpslab1.util.SpringContext;
import org.jboss.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.Map;

public class JaasLoginModule implements LoginModule {
    private static final Logger log = Logger.getLogger(JaasLoginModule.class);

    private Subject subject;
    private CallbackHandler callbackHandler;
    private UserRepository userRepository;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        log.debug("Initializing JaasLoginModule");
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.userRepository = SpringContext.getBean(UserRepository.class);
    }

    @Override
    public boolean login() throws LoginException {
        try {
            log.debug("Performing JAAS login");

            NameCallback nameCallback = new NameCallback("username");
            PasswordCallback passwordCallback = new PasswordCallback("password", false);

            callbackHandler.handle(new Callback[]{nameCallback, passwordCallback});

            String username = nameCallback.getName();
            String password = new String(passwordCallback.getPassword());

            log.debugf("Attempting to authenticate user: %s", username);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder =
                    SpringContext.getBean(org.springframework.security.crypto.password.PasswordEncoder.class);

            if (!passwordEncoder.matches(password, user.getPassword())) {
                log.warnf("Authentication failed for user '%s': invalid credentials", username);
                throw new InvalidCredentialsException("Invalid credentials");
            }

            log.infof("Authentication successful for user: %s", username);
            subject.getPrincipals().add(new UserPrincipal(user.getUsername()));
            return true;

        } catch (IOException | UnsupportedCallbackException e) {
            log.error("Callback error during JAAS login", e);
            throw new LoginException("Callback error: " + e.getMessage());
        }
    }

    @Override
    public boolean commit() throws LoginException {
        log.debug("Committing JAAS login");
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        log.debug("Aborting JAAS login");
        return false;
    }

    @Override
    public boolean logout() throws LoginException {
        log.debug("Logging out JAAS user");
        return true;
    }
}
