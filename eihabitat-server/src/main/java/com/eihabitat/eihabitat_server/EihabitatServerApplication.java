package com.eihabitat.eihabitat_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class EihabitatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EihabitatServerApplication.class, args);
    }

}

