package com.example.demo.config;

import com.example.demo.servlet.SimpleStatusServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean<SimpleStatusServlet> simpleStatusServletRegistration() {
        return new ServletRegistrationBean<>(
                new SimpleStatusServlet(),
                "/status"
        );
    }
}
