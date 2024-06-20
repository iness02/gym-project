package com.example.GymProject.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;

@Configuration
public class TestConfig {
    @Bean
    public ServletContext servletContext() {
        return Mockito.mock(ServletContext.class);
    }
}