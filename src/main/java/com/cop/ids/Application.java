package com.cop.ids;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.SpringVersion;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;

@SpringBootApplication()
@EnableAsync
@ComponentScan(basePackages = "com.cop.ids")
public class Application {

    public static void main(String[] args) {
    	//System.out.println("version: " + SpringVersion.getVersion());
        SpringApplication.run(Application.class, args);
        
    }

}
