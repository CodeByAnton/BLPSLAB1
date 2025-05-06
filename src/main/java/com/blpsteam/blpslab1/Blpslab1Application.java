package com.blpsteam.blpslab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Blpslab1Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        System.setProperty("java.security.auth.login.config",
                Blpslab1Application.class.getClassLoader().getResource("jaas.config").toString());
        SpringApplication.run(Blpslab1Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Blpslab1Application.class);
    }

}
